import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class SaveDataController {
    public static final String separator = "\0|";
    private static final String separatorRegex = "\0\\|";
    private static String saveDir = "data";
    private static String saveFile = "tasks.txt";

    public static boolean saveTaskData(List<Task> tasks) {
        String dataString = tasksToDataString(tasks);

        try {
            // note that this will do nothing if the directory exists, or create a new one if it does not exist
            Files.createDirectories(Paths.get(System.getProperty("user.dir"), saveDir));
        } catch (IOException e) {
            System.out.println("Unable to create directory for save file");
            return false;
        }

        try (FileWriter fw = new FileWriter(Paths.get(System.getProperty("user.dir"), saveDir, saveFile).toString())) {
            fw.write(dataString);
        } catch (IOException e) {
            System.out.println("Unable to write to file.");
            return false;
        }

        return true;
    }

    public static boolean readTaskDataTo(ArrayList<Task> tasks) {
        List<String> savedTasks;
        boolean success = true;
        try (FileReader fr = new FileReader(Paths.get(System.getProperty("user.dir"), saveDir, saveFile).toString())) {
            savedTasks = fr.readAllLines();
            for (String taskString : savedTasks) {
                try {
                    tasks.add(dataStringToTask(taskString));
                } catch (ParseException | IllegalArgumentException e) {
                    success = false;
                }
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    private static String tasksToDataString(List<Task> tasks) {
        StringBuilder dataString = new StringBuilder();
        for (Task task : tasks) {
            dataString.append(String.join(separator,task.toDataList()));
            dataString.append('\n');
        }
        return dataString.toString();
    }

    private static Task dataStringToTask(String dataString) throws ParseException, IllegalArgumentException {
        String[] data = dataString.split(separatorRegex);
        return TaskFactory.makeTaskFromData(data);
    }
}
