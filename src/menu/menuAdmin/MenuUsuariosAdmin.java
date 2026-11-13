package menu.menuAdmin;

import dao.UsuarioDao;
import excepciones.AccionNoPermitidaException;
import modelo.Administrador;
import modelo.Jugador;
import modelo.Usuario;
import modelo.enums.Rol;
import servicio.RegistroServicio;
import util.Consola;
import util.Seguridad;

import java.sql.SQLException;
import java.util.List;


public class MenuUsuariosAdmin {

    private final Administrador administrador;
    private final UsuarioDao usuarioDao;
    private final RegistroServicio registroServicio;


    public MenuUsuariosAdmin(Administrador administrador) {
        this.administrador = administrador;
        this.usuarioDao = new UsuarioDao();
        this.registroServicio = new RegistroServicio();
    }

    public void mostrar() {
        int opcion;

        do {
            System.out.println("""

                    === GESTION DE USUARIOS ===
                    1. Alta de usuario
                    2. Modificar usuario
                    3. Baja logica de usuario
                    4. Consultar usuario por ID
                    5. Listar usuarios activos
                    0. Volver
                    """);

            opcion = Consola.leerEntero("Opcion: ", 0, 5);

            try {

                switch (opcion) {
                    case 1 -> altaUsuario();
                    case 2 -> modificarUsuario();
                    case 3 -> bajaUsuario();
                    case 4 -> consultarUsuario();
                    case 5 -> listarUsuarios();
                    case 0 -> System.out.println(
                            "Volviendo al menu administrador..."
                    );
                }

            } catch (AccionNoPermitidaException | IllegalArgumentException e) {

                /*
                 * AccionNoPermitidaException puede venir de RegistroServicio.
                 * IllegalArgumentException puede venir de las validaciones
                 * de Usuario, Jugador, Administrador o Seguridad.
                 */
                System.out.println(
                        "No se pudo realizar la operacion: "
                                + e.getMessage()
                );

            } catch (SQLException e) {
                System.out.println(
                        "Ocurrio un error al acceder a la base de datos."
                );
            }

            if (opcion != 0) {
                Consola.pausar();
            }

        } while (opcion != 0);
    }


    /**
     * Da de alta un nuevo usuario.
     *
     * Si se crea un JUGADOR uso RegistroServicio porque el registro
     * de un jugador no consiste solamente en insertar un usuario:
     * tambien se crea su partida e inventario inicial dentro
     * de una misma transaccion.
     *
     * Si se crea un ADMIN, solamente necesito guardar el usuario.
     */
    private void altaUsuario() throws SQLException, AccionNoPermitidaException {
        System.out.println("\n--- ALTA DE USUARIO ---");

        Rol rol = leerRol();

        String nombreUsuario =
                Consola.leerTexto("Nombre de usuario: ");

        String email =
                Consola.leerTextoOpcional(
                        "Email (opcional): "
                );

        String contrasenia =
                Consola.leerTexto("Contrasenia: ");

        if (rol == Rol.JUGADOR) {

            /*
             * Para un jugador tambien necesito el nombre de su granja.
             * RegistroServicio se ocupa de:
             * - validar usuario y email repetidos
             * - hashear la contrasenia
             * - crear el Jugador
             * - crear la Partida
             * - crear el Inventario inicial
             * - hacer rollback si alguna operacion falla
             */
            String nombreGranja =
                    Consola.leerTexto("Nombre de la granja: ");

            Jugador jugador = registroServicio.registrar(
                    nombreUsuario,
                    email,
                    contrasenia,
                    nombreGranja
            );

            System.out.println("Jugador creado correctamente. ID generado: "
                            + jugador.getIdUsuario()
            );

            return;
        }


        if (usuarioDao.existeNombre(nombreUsuario)) {
            throw new AccionNoPermitidaException(
                    "Ese nombre de usuario ya existe."
            );
        }

        if (email != null && usuarioDao.existeEmail(email)) {
            throw new AccionNoPermitidaException(
                    "Ese email ya existe."
            );
        }

        String hash = Seguridad.hashear(contrasenia);

        Administrador nuevoAdministrador = new Administrador(
                nombreUsuario,
                email,
                hash
        );

        if (usuarioDao.alta(nuevoAdministrador)) {

            System.out.println(
                    "Administrador creado correctamente. ID generado: "
                            + nuevoAdministrador.getIdUsuario()
            );

        } else {

            System.out.println(
                    "No se pudo crear el administrador."
            );
        }
    }


    /**
     * Modifica los datos de un usuario existente.
     *
     * El rol NO se modifica porque UsuarioDao.modificar()
     * actualmente actualiza solamente:
     * - nombre_usuario
     * - email
     * - contrasenia_hash
     *
     * Si se deja la nueva contrasenia vacia, mantengo el hash actual.
     */
    private void modificarUsuario() throws SQLException {

        System.out.println("\n--- MODIFICAR USUARIO ---");

        int id = Consola.leerEntero(
                "ID del usuario: ",
                1,
                Integer.MAX_VALUE
        );

        Usuario usuarioActual =
                usuarioDao.consultarPorId(id);

        if (usuarioActual == null) {

            System.out.println(
                    "No existe un usuario con ese ID."
            );

            return;
        }

        if (!usuarioActual.isActivo()) {

            System.out.println(
                    "El usuario esta dado de baja y no puede modificarse."
            );

            return;
        }

        System.out.println("Datos actuales:");
        mostrarUsuario(usuarioActual);

        System.out.println("\nIngrese los nuevos datos:");

        String nuevoNombre =
                Consola.leerTexto("Nombre de usuario: ");

        String nuevoEmail =
                Consola.leerTextoOpcional(
                        "Email (opcional): "
                );

        String nuevaContrasenia =
                Consola.leerTextoOpcional(
                        "Nueva contrasenia (Enter = mantener actual): "
                );


        if (!nuevoNombre.equalsIgnoreCase(
                usuarioActual.getNombreUsuario()
        ) && usuarioDao.existeNombre(nuevoNombre)) {

            System.out.println(
                    "Ese nombre de usuario ya esta en uso."
            );

            return;
        }

        if (nuevoEmail != null) {

            String emailActual = usuarioActual.getEmail();

            boolean emailCambio =
                    emailActual == null
                            || !nuevoEmail.equalsIgnoreCase(emailActual);

            if (emailCambio && usuarioDao.existeEmail(nuevoEmail)) {

                System.out.println(
                        "Ese email ya esta en uso."
                );

                return;
            }
        }


        usuarioActual.setNombreUsuario(nuevoNombre);
        usuarioActual.setEmail(nuevoEmail);

        if (nuevaContrasenia != null) {
            usuarioActual.setContraseniaHash(
                    Seguridad.hashear(nuevaContrasenia)
            );
        }

        if (usuarioDao.modificar(usuarioActual)) {

            /*
             * Si el administrador modifico su propio usuario,
             * actualizo tambien el objeto que representa
             * la sesion actual para no dejar datos viejos en memoria.
             */
            if (usuarioActual.getIdUsuario()
                    == administrador.getIdUsuario()) {

                administrador.setNombreUsuario(
                        usuarioActual.getNombreUsuario()
                );

                administrador.setEmail(
                        usuarioActual.getEmail()
                );

                administrador.setContraseniaHash(
                        usuarioActual.getContraseniaHash()
                );
            }

            System.out.println(
                    "Usuario modificado correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo modificar el usuario."
            );
        }
    }


    private void bajaUsuario() throws SQLException {

        System.out.println("\n--- BAJA DE USUARIO ---");

        int id = Consola.leerEntero(
                "ID del usuario: ",
                1,
                Integer.MAX_VALUE
        );

        if (id == administrador.getIdUsuario()) {

            System.out.println(
                    "No podes dar de baja al administrador "
                            + "que tiene la sesion iniciada."
            );

            return;
        }

        Usuario usuario =
                usuarioDao.consultarPorId(id);

        if (usuario == null) {

            System.out.println(
                    "No existe un usuario con ese ID."
            );

            return;
        }

        if (!usuario.isActivo()) {

            System.out.println(
                    "El usuario ya se encuentra dado de baja."
            );

            return;
        }

        mostrarUsuario(usuario);

        if (!Consola.confirmar(
                "¿Confirma la baja del usuario?"
        )) {

            System.out.println(
                    "Operacion cancelada."
            );

            return;
        }

        if (usuarioDao.bajaLogica(id)) {

            System.out.println(
                    "Usuario dado de baja correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo dar de baja el usuario."
            );
        }
    }


    /**
     * Consulta un usuario mediante su ID.
     *
     * UsuarioDao.consultarPorId() no filtra por activo,
     * por lo que tambien permite consultar usuarios dados de baja.
     */
    private void consultarUsuario() throws SQLException {

        System.out.println("\n--- CONSULTAR USUARIO ---");

        int id = Consola.leerEntero(
                "ID del usuario: ",
                1,
                Integer.MAX_VALUE
        );

        Usuario usuario =
                usuarioDao.consultarPorId(id);

        if (usuario == null) {

            System.out.println(
                    "No existe un usuario con ese ID."
            );

            return;
        }

        mostrarUsuario(usuario);
    }


    private void listarUsuarios() throws SQLException {

        System.out.println("\n--- USUARIOS ACTIVOS ---");

        List<Usuario> usuarios =
                usuarioDao.listar();

        if (usuarios.isEmpty()) {

            System.out.println(
                    "No hay usuarios activos para mostrar."
            );

            return;
        }

        for (Usuario usuario : usuarios) {

            mostrarUsuario(usuario);

            System.out.println(
                    "------------------------------"
            );
        }
    }



    private Rol leerRol() {

        System.out.println("Tipo de usuario:");
        System.out.println("1. JUGADOR");
        System.out.println("2. ADMIN");

        int opcion = Consola.leerEntero(
                "Tipo: ",
                1,
                2
        );

        return opcion == 1
                ? Rol.JUGADOR
                : Rol.ADMIN;
    }


    /**
     * Centralizo la forma de mostrar un Usuario.
     *
     * A proposito NO muestro contraseniaHash.
     * Aunque sea un hash y no la contrasenia original,
     * no es informacion que necesite mostrarse en el menu.
     */
    private void mostrarUsuario(Usuario usuario) {

        System.out.println(
                "ID: " + usuario.getIdUsuario()
        );

        System.out.println(
                "Nombre de usuario: "
                        + usuario.getNombreUsuario()
        );

        System.out.println(
                "Email: "
                        + (
                        usuario.getEmail() == null
                                ? "Sin email"
                                : usuario.getEmail()
                )
        );

        System.out.println(
                "Rol: " + usuario.getRol()
        );

        System.out.println(
                "Activo: "
                        + (
                        usuario.isActivo()
                                ? "Si"
                                : "No"
                )
        );

        System.out.println(
                "Fecha de creacion: "
                        + usuario.getFechaCreacion()
        );
    }
}