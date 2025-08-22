package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import modelo.Usuario;

public class UsuariosRepo {
    private static final String FILE = "usuarios.dat";

    public static List<Usuario> cargarTodo() {
        List<Usuario> lista = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return lista;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            while (true) {
                Usuario u = (Usuario) in.readObject(); // acceso secuencial
                lista.add(u);
            }
        } catch (EOFException eof) {
            // fin de archivo: es lo esperado
        	System.out.println("Fin de archivo alcanzado y procesado correctamente.");
        } catch (Exception e) {
            e.printStackTrace(); // maneja según tu app
        }
        return lista;
    }

    public static void guardarTodo(List<Usuario> lista) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            for (Usuario u : lista) {
                out.writeObject(u); // secuencial
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refrescar(DefaultTableModel model, List<Usuario> datos) {
        model.setRowCount(0);
        for (Usuario u : datos) {
            model.addRow(new Object[]{u.getPerfil(), u.getNombre(), u.getClave()});
        }
    }

    // Operaciones CRUD simples
    public void agregar(Usuario u, List<Usuario> datos, DefaultTableModel model) {
        datos.add(u);
        UsuariosRepo.guardarTodo(datos); // reescribe el .dat completo
        refrescar(model, datos);
    }

    public void eliminarPorIndice(int row, List<Usuario> datos, DefaultTableModel model) {
        if (row < 0) return;
        datos.remove(row);
        UsuariosRepo.guardarTodo(datos);
        refrescar(model, datos);
    }
    // Modificar un usuario por índice
    public void modificarPorIndice(int row, Usuario u, List<Usuario> datos, DefaultTableModel model) {
		if (row < 0 || row >= datos.size()) return;
		datos.set(row, u);
		UsuariosRepo.guardarTodo(datos);
		refrescar(model, datos);
	}

    
}

