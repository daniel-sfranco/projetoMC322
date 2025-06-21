package fileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PlaylistFileManager {
    private static String PLAYLIS_FILE_LOCATION = "User" + File.separator + "PlaylistsIds.che";

    public static void addPlaylistId(String playlistId) {
        ArrayList<String> lines = new ArrayList<String>(
            Arrays.asList(playlistId));

        FileCheManager.addData(lines, PLAYLIS_FILE_LOCATION);
    }

    public static ArrayList<String> getUserPlaylists() {
        return FileCheManager.readCheFile(PLAYLIS_FILE_LOCATION);
    }

    public static void deletePlaylistId(String playlistId) {
        FileCheManager.deleteLines(playlistId, PLAYLIS_FILE_LOCATION);
    }

    public static void main(String[] args) {
        // PlaylistFileManager.addPlaylistId("playlist1");
        // PlaylistFileManager.addPlaylistId("playlist2");
        // PlaylistFileManager.addPlaylistId("playlist3");

        PlaylistFileManager.deletePlaylistId("playlist3");
        System.out.println(PlaylistFileManager.getUserPlaylists());
    }
}
