package main.me.geakstr.engine.model;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Models {

    private HashMap<String, Model> models;

    public Models() {
        String pathToModels = "src/resources/models/";
        models = new HashMap();
        File directory = new File(pathToModels);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                models.put(file.getName(), new Model(pathToModels + file.getName()));
            }
        }
    }

    public HashMap<String, Model> getModels() {
        return models;
    }

    public Model getModel(String modelName) {
        return models.get(modelName);
    }

    public ArrayList<Model> getAllModels() {
        ArrayList<Model> allModels = new ArrayList();
        for(Map.Entry<String, Model> e : models.entrySet()) {
            allModels.add(e.getValue());
        }
        return allModels;
    }

    public ArrayList<String> getAllModelNames() {
        ArrayList<String> allModels = new ArrayList();
        for(Map.Entry<String, Model> e : models.entrySet()) {
            allModels.add(e.getKey());
        }
        return allModels;
    }

}