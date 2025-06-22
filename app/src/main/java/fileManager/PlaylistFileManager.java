package fileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PlaylistFileManager extends FileManager {
    static {
        FileManager.SPECIFIC_LOCATION = FileManager.FILES_LOCATION + "User" + File.separator + "PlaylistsIds.che";
    }

    public static void addPlaylistId(String playlistId) {
        ArrayList<String> lines = new ArrayList<String>(
            Arrays.asList(playlistId));

        FileManager.addData(lines);
    }

    public static ArrayList<String> getUserPlaylists() {
        return FileManager.readFile();
    }

    public static void deletePlaylistId(String playlistId) {
        FileManager.deleteLines(playlistId);
    }

    public static void main(String[] args) {
        // PlaylistFileManager.addPlaylistId("playlist1");
        // PlaylistFileManager.addPlaylistId("playlist2");
        // PlaylistFileManager.addPlaylistId("playlist3");

        PlaylistFileManager.deletePlaylistId("playlist3");
        System.out.println(PlaylistFileManager.getUserPlaylists());
    }
}
