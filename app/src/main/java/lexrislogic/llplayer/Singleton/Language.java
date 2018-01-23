package lexrislogic.llplayer.Singleton;

public class Language {
    public final static int CODE_ES = 1;
    public final static int CODE_EN = 2;
    private static Language language_instance = new Language( );
    private int language;
    public String AddPlayListItem;
    public String ConfigurationItem;
    public String UserManualItem;
    public String AddEqualizerItem;
    public String AddSongsItem;

    public String PlayListTab;
    public String BrowseTab;
    public String MaintenanceTab;

    public String CurrentPlayList;
    public String CurrentPlayListSubText;
    public String AllSongs;
    public String Favorites;
    public String FavoritesSubText;
    public String MostPlayedSongs;
    public String MostPlayedSongsSubText;

    public String EmptyListText;
    public String NoAvailableLyricsInFile;

    public String BackMediaPlayerItem;
    public String CloseMediaPlayerItem;
    public String PrevSongPlayerItem;
    public String NextSongPlayerItem;
    public String PlayPauseSongPlayerItem;
    public String ShuffleMediaPlayerItem;
    public String EqualizerText;
    public String OnShuffle;
    public String OffShuffle;
    public String RepeatMediaPlayerItem;
    public String RepeatOff;
    public String RepeatList;
    public String RepeatSong;
    public String Playing;
    public String Paused;

    public String messageInvalid;
    public String invalidNamePlayMode;
    public String invalidNamePlayList;
    public String cantModEq;
    public String cantDelEq;

    public String timerDialogTittle;
    public String timerDialogMessage;
    public String timerStopText;
    public String timerPlayText;
    public String timerCanceled;
    public String invalidTimeTimer;
    public String timerActivated;
    public String timerActivatedMessage;

    public String acceptDialog;
    public String cancelDialog;
    public String messageDeleted;
    public String removePlayModeEq;
    public String createdPlayModeEq;
    public String modPlayModeEq;
    public String NewEqualizerText;
    public String EqualizerTextTitle;
    public String CreatePlayModeText;
    public String ModPlayModeText;

    public String newSongs;
    public String updatedSongs;

    public String MaintenanceNewSongsText;
    public String MaintenanceNewSongsSubText;
    public String MaintenanceLostSongsText;
    public String MaintenanceLostSongsSubText;
    public String MaintenanceReplaceSongText;
    public String MaintenanceReplaceSongSubText;
    public String MaintenanceRemoveSongText;
    public String MaintenanceRemoveSongSubText;

    public String SettingsThemeText;
    public String SettingsThemeSubText;
    public String SettingsLanguageText;
    public String SettingsLanguageSubText;
    public String SettingsPlayerText;
    public String SettingsPlayerSubText;
    public String LanguageESText;
    public String LanguageESSubText;
    public String LanguageENText;
    public String LanguageENSubText;

    public String ColorBackgroundPrimaryText;
    public String ColorBackgroundPrimarySubText;
    public String ColorBackgroundSecondaryText;
    public String ColorBackgroundSecondarySubText;
    public String ColorPrimaryFirstTextText;
    public String ColorPrimaryFirstTextSubText;
    public String ColorPrimarySecondTextText;
    public String ColorPrimarySecondTextSubText;
    public String ColorSecondaryFirstTextText;
    public String ColorSecondaryFirstTextSubText;
    public String ColorSecondarySecondTextText;
    public String ColorSecondarySecondTextSubText;
    public String setColor;
    public String lastDateMod;
    public String lostOptionsDialogTittle;
    public String newPlayListName;
    public String CreatePlayListText;

    public String RenPlayListText;
    public String RenPlayListSubText;
    public String ModPlayListText;
    public String ModPlayListSubText;
    public String RemPlayListText;
    public String RemPlayListSubText;

    public String modPlayList;
    public String removePlayList;

    public String messageReplaced;
    public String replaceText;
    public String removeSong;

    public String areYouSureReplace;
    public String areYouSureReplaceby;

    public String BrowseArtistText;
    public String BrowseArtistSubText;
    public String BrowseAlbumText;
    public String BrowseAlbumSubText;
    public String BrowseGenreText;
    public String BrowseGenreSubText;
    public String ArtistTotalAlbum;
    public String GenreTotalSongs;

    public String addSongsPlayList;

    public String PlayerSettingAutoStop;
    public String PlayerSettingSaveBackup;
    public String PlayerSettingLoadBackup;

    public String UserManualMediaPlayerText;
    public String UserManualMediaPlayerSubText;
    public String UserManualMediaPlayerContent;

    public String UserManualSongManagementText;
    public String UserManualSongManagementSubText;
    public String UserManualSongManagementContent;

    public String UserManualPlayListText;
    public String UserManualPlayListSubText;
    public String UserManualPlayListContent;

    public String UserManualPlayModeText;
    public String UserManualPlayModeSubText;
    public String UserManualPlayModeContent;

    public String SongShareText;
    public String SongShareSubText;
    public String SongShareMessage;
    public String SongRingtoneText;
    public String SongRingtoneSubText;
    public String SongRingtoneMessage;
    public String SongRemoveText;
    public String SongRemoveSubText;
    public String SongRemoveMessage;
    public String errorRemoveSong;
    public String cantRemoveSong;
    public String successRemoveSong;

    public String successBackup;
    public String errorBackup;

    public String totalSongMessage;
    public String errorLoadingCloseMP;
    public String errorLoadingFileMP;
    public String successRefreshBD;
    public String LoadBackupMessage;
    public String PlayerSettingAutoReplay;

    public String BackupText;
    public String pathText;
    public String anyPath;
    public String startBackup;
    public String replaceTextBackup;

    private Language() {
    }
    public static Language getInstance() {
        return language_instance;
    }
    public int get_language() {
        return language;
    }
    public void set_language(int new_language){
        language=new_language;
        switch(language)
        {
            case CODE_ES:
                AddPlayListItem = "Crear Lista de Reproducción";
                ConfigurationItem = "Configuración";
                UserManualItem = "Manual de Usuario";
                PlayListTab = "Listas de Reproducción";
                BrowseTab = "Navegar";
                MaintenanceTab = "Mantenimiento";
                CurrentPlayList = "Lista Actual";
                CurrentPlayListSubText = "Última Lista Reproducida";
                AllSongs = "Todas las Canciones";
                Favorites = "Favoritos";
                FavoritesSubText = "Canciones Favoritas";
                MostPlayedSongs = "Más Reproducidas";
                MostPlayedSongsSubText = "Una Lista de las 25 Canciones Más Reproducidas";
                EmptyListText = "Sin Resultados";
                NoAvailableLyricsInFile = "No Hay Letra Disponible\n\n\n\n\n";
                BackMediaPlayerItem = "Atrás";
                CloseMediaPlayerItem = "Detener";
                PrevSongPlayerItem = "Anterior";
                NextSongPlayerItem = "Siguiente";
                PlayPauseSongPlayerItem = "Reproducir/Pausar";
                ShuffleMediaPlayerItem = "Aleatorio";
                OnShuffle = "Aleatorio Encendido";
                OffShuffle = "Aleatorio Apagado";
                RepeatMediaPlayerItem = "Repeticiones";
                RepeatOff = "No Repetir Lista";
                RepeatList = "Repitiendo Lista";
                RepeatSong = "Repitiendo Canción Actual";
                Playing = "Reproduciendo";
                Paused = "En Pausa";
                messageInvalid = "Lista Sin Canciones Válidas";
                timerDialogTittle = "Temporizador";
                timerDialogMessage = "Tiempo";
                timerStopText = "Detener";
                timerPlayText = "Iniciar";
                timerCanceled = "Temporizador Cancelado";
                invalidTimeTimer = "Tiempo no Válido";
                timerActivated = "Temporizador Activado";
                timerActivatedMessage = "El Reproductor de Medios fue Detenido por el Temporizador";
                EqualizerText = "Ecualizador";
                AddEqualizerItem = "Crear Modo de Reproducción del Ecualizador";
                AddSongsItem = "Añadir Canciones";
                cantModEq="No se puede Modificar este Modo de Reproducción del Ecualizador";
                cantDelEq="No se puede Eliminar este Modo de Reproducción del Ecualizador";
                acceptDialog="Aceptar";
                cancelDialog="Cancelar";
                messageDeleted="Removido";
                createdPlayModeEq="Creado";
                modPlayModeEq="Modificado";
                removePlayModeEq="Remover Modo de Reproducción del Ecualizador";
                newSongs="Canciones Nuevas";
                updatedSongs="Canciones Actualializadas";
                NewEqualizerText="Nuevo Ecualizador";
                EqualizerTextTitle="Nombre del Modo de Reproducción";
                CreatePlayModeText="Crear";
                ModPlayModeText="Modificar";
                invalidNamePlayMode="Modo de Reproducción sin Nombre";
                invalidNamePlayList="Lista de Reproducción sin Nombre";
                MaintenanceNewSongsText="Canciones Nuevas y Modificadas";
                MaintenanceNewSongsSubText="Canciones Agregadas o Modificadas Recientemente";
                MaintenanceLostSongsText="Canciones Extraviadas";
                MaintenanceLostSongsSubText="Canciones No Encontradas por el Sistema de Archivos";
                SettingsThemeText="Tema";
                SettingsThemeSubText="Cambiar los Colores de la Aplicación";
                SettingsLanguageText="Idioma";
                SettingsLanguageSubText="Cambiar el Idioma de la Aplicación";
                SettingsPlayerText="Opciones del Reproductor";
                SettingsPlayerSubText="Cambia las Opciones del Reproductor";
                LanguageESText="Español";
                LanguageESSubText="Cambiar el Idioma de la Aplicación al Español";
                LanguageENText="Inglés";
                LanguageENSubText="Cambiar el Idioma de la Aplicación al Inglés";
                ColorBackgroundPrimaryText="Color Principal del Fondo";
                ColorBackgroundPrimarySubText="Cambia el Color del Fondo de la Barra de Opciones y Color Principal de la Aplicación";
                ColorBackgroundSecondaryText="Color Secundario del Fondo";
                ColorBackgroundSecondarySubText="Cambia el Color Secundario del Fondo del Reproductor";
                ColorPrimaryFirstTextText="Color Principal del Texto de Objetos Principales";
                ColorPrimaryFirstTextSubText="Cambia el Color Principal del Texto de la Barra de Opciones";
                ColorPrimarySecondTextText="Color Secundario del Texto de Objetos Principales";
                ColorPrimarySecondTextSubText="Cambia el Color Secundario del Texto de la Barra de Opciones";
                ColorSecondaryFirstTextText="Color Principal del Texto de Objetos Secundarios";
                ColorSecondaryFirstTextSubText="Cambia el Color Principal del Texto de los Elementos como Listas, Letras";
                ColorSecondarySecondTextText="Color Secundario del Texto de Objetos Secundarios";
                ColorSecondarySecondTextSubText="Cambia el Color Secundario del Texto de los Elementos de una Lista";
                setColor="Cambiar Color";
                lastDateMod="Modificado por Última Vez: ";
                lostOptionsDialogTittle="Opciones";
                MaintenanceReplaceSongText="Reemplazar Canción Extraviada";
                MaintenanceReplaceSongSubText="Reemplaza esta Canción Extraviada con Otra en todas las Listas de Reproducción";
                MaintenanceRemoveSongText="Eliminar Canción Extraviada";
                MaintenanceRemoveSongSubText="Elimina la referencia a la Canción Extraviada en todas las Listas de Reproducción";
                newPlayListName="Nueva Lista de Reproducción";
                CreatePlayListText="Crear Lista de Reproducción";
                RenPlayListText="Renombrar Lista de Reproducción";
                RenPlayListSubText="Cambia el Nombre de la Lista de Reproducción";
                ModPlayListText="Modificar Lista de Reproducción";
                ModPlayListSubText="Añade, Mueve y Quita Canciones en la Lista de Reproducción";
                RemPlayListText="Eliminar Lista de Reproducción";
                RemPlayListSubText="Elimina la Lista de Reproducción Creada";
                modPlayList="Renombrado";
                removePlayList="Remover Lista de Reproducción";
                messageReplaced="Reemplazado";
                replaceText="Reemplazar: ";
                removeSong="Remover Canción Extraviada";
                areYouSureReplace="Esta Seguro de Reemplazar:";
                areYouSureReplaceby="Por:";
                BrowseArtistText="Por Artista";
                BrowseArtistSubText="Buscar Canciones por Artista";
                BrowseAlbumText="Por Álbum";
                BrowseAlbumSubText="Buscar Canciones por Álbum";
                BrowseGenreText="Por Género";
                BrowseGenreSubText="Buscar Canciones por Género";
                addSongsPlayList="Añadir Canciones";
                ArtistTotalAlbum="Álbum(es)";
                GenreTotalSongs="Canción(es)";
                PlayerSettingAutoStop="Pausar Reproducción al Desconectar Auriculares";
                UserManualMediaPlayerText="Reproductor";
                UserManualMediaPlayerSubText="Acerca del Reproductor";
                UserManualMediaPlayerContent="LexRis Logic 2017\n" +
                        "El reproductor recibe un conjunto de canciones para ser reproducidas, " +
                        "gestionadas y controladas con el fin de sacar información extra para las listas de reproducción " +
                        "de más reproducidas o favoritos.\n" +
                        "La lista de reproducción que actualmente tiene el reproductor no se actualiza por cambios de la " +
                        "lista de reproducción original.\n" +
                        "El reproductor tiene una opción para pausar la reproducción si los auriculares son desconectados, " +
                        "puede encontrar la opción en configuración.\n" +
                        "Cuando se vuelve a abrir el reproductor los datos recuperados para la lista de reproducción actual " +
                        "son:\n" +
                        "- El conjunto de canciones.\n" +
                        "- El título del conjunto de canciones.\n" +
                        "- La última canción reproducida.\n" +
                        "- La posición de la última canción.\n" +
                        "- El modo de reproducción del ecualizador elegido.\n" +
                        "Puede guardar una copia de la base de datos del reproductor para luego ser cargada en ese estado" +
                        ", asi podra guardar todas sus listas de reproducción y modos de reproducción del ecualizador.\n";
                UserManualSongManagementText="Administración de las Canciones";
                UserManualSongManagementSubText="Acerca de las Canciones";
                UserManualSongManagementContent="Al abrir la aplicación se leeran las nuevas canciones y sus actualizaciones; una " +
                        "canción nueva es aquella que tiene una nueva ruta y nombre de archivo, y una canción actualizada es aquella " +
                        "que sufrió alguna modificación pero sigue manteniendo la misma ruta y nombre de archivo.\n" +
                        "Cuando una canción es eliminada en el sistema de archivos, esta no se dará por eliminada en el reproductor, " +
                        "sino sera una canción perdida, estas canciones se pueden visualizar en la sección de mantenimiento para poder " +
                        "elegir alguna de las dos opciones:\n" +
                        "- Eliminar la canción, actualizando así todas las listas de reproducción que la contenian.\n" +
                        "- Reemplazar la canción con una canción válida, sustituyendo en todas las listas de reproducción la canción " +
                        "perdida por su reemplazo.\n" +
                        "Tomar en cuenta si una canción perdida vuelve a ser hallada usando su ruta y nombre de archivo, es decir, vuelve a " +
                        "aparecer en el sistema de archivos del celular; automáticamente será reemplazada actualizando sus datos, por si " +
                        "hubiese alguna actualización, y no necesitara hacer un reemplazo manual. Puede abusar de esta opción para manejar la " +
                        "aplicación como una biblioteca de música.";
                UserManualPlayListText="Lista de Reproducción";
                UserManualPlayListSubText="Acerca de las Listas de Reproducción";
                UserManualPlayListContent="Las listas de reproducción se crean con los índices asigandos a cada canción, " +
                        "para que cuando se reemplaze una canción por otra, en la parte de mantenimiento, automaticamente se reemplaze en " +
                        "las listas de reproducción.";
                UserManualPlayModeText="Modos de Reproducción del Ecualizador";
                UserManualPlayModeSubText="Acerca del Ecualizador";
                UserManualPlayModeContent="Los modos de reproducción del ecualizador se pueden elegir en la pantalla del reproductor, " +
                        "el usuario podrá crear más modos de reproducción, si quiere crear un modo basado en otro, solo seleccione " +
                        "el modo de reproducción del que se basará y luego elija la opción de crear nuevo modo de reproducción.";
                SongShareText="Compartir";
                SongShareSubText="Compartir el archivo de audio";
                SongShareMessage="Compartir";
                SongRingtoneText="Establecer como Tono de Llamada";
                SongRingtoneSubText="Establace la canción elegida como Tono de Llamada";
                SongRingtoneMessage="Nuevo Tono de Llamada:\n";
                SongRemoveText="Eliminar Canción";
                SongRemoveSubText="Elimina la canción del sistema de archivos";
                SongRemoveMessage="Esta seguro de Eliminar ";
                errorRemoveSong="No se puede eliminar la canción que actualmente se esta reproduciendo";
                successRemoveSong=" Eliminado";
                cantRemoveSong="Error Eliminando el Archivo";
                PlayerSettingSaveBackup="Guardar Inf. del Reproductor";
                PlayerSettingLoadBackup="Cargar Inf. del Reproductor";
                successBackup="Copia Creada en: ";
                errorBackup="Error Creando la copia de información.";
                totalSongMessage="Total de Canciones: ";
                errorLoadingCloseMP="Apague el reproductor antes de cargar Copia";
                errorLoadingFileMP="No se encontro el archivo en la memoria del teléfono: ";
                successRefreshBD="Se Actualizo la información";
                LoadBackupMessage="Cargar una copia eliminara toda la información actual, ¿Desea continuar?";
                PlayerSettingAutoReplay="Repetir Canción con el Botón Retroceder Canción";
                BackupText="Copia de Información";
                pathText="Ubicación: ";
                anyPath="No cambiar";
                startBackup="Cargar Copia";
                replaceTextBackup="Cambiar Por:";
                break;
            case CODE_EN:
                AddPlayListItem = "Create PlayList";
                ConfigurationItem = "Configuration";
                UserManualItem = "User Manual";
                PlayListTab = "PlayLists";
                BrowseTab = "Browse";
                MaintenanceTab = "Maintenance";
                CurrentPlayList = "Current Playlist";
                CurrentPlayListSubText = "Last Played List";
                AllSongs = "All Songs";
                Favorites = "Bookmarks";
                FavoritesSubText = "Favorite Songs";
                MostPlayedSongs = "Most Played Songs";
                MostPlayedSongsSubText = "A List of the 25 Most Played Songs";
                EmptyListText = "No Results";
                NoAvailableLyricsInFile = "No Lyrics Available\n\n\n\n\n";
                BackMediaPlayerItem = "Back";
                CloseMediaPlayerItem = "Stop";
                PrevSongPlayerItem = "Previous";
                NextSongPlayerItem = "Next";
                PlayPauseSongPlayerItem = "Play/Pause";
                ShuffleMediaPlayerItem = "Shuffle";
                OnShuffle = "Shuffle On";
                OffShuffle = "Shuffle Off";
                RepeatMediaPlayerItem = "Repeats";
                RepeatOff = "No Repeat List";
                RepeatList = "Repeating List";
                RepeatSong = "Repeating Current Song";
                Playing = "Playing";
                Paused = "In Pause";
                messageInvalid = "PlayList Without Valid Songs";
                timerDialogTittle = "Timer";
                timerDialogMessage= "Time";
                timerStopText = "Stop";
                timerPlayText = "Start";
                timerCanceled = "Timer Canceled";
                invalidTimeTimer = "Invalid Time";
                timerActivated = "Timer On";
                timerActivatedMessage = "The Media Player was stopped by the Timer";
                EqualizerText = "Equalizer";
                AddEqualizerItem = "Create a Equalizer Play Mode";
                AddSongsItem = "Add Songs";
                cantModEq="Can't Modify this Equalizer Play Mode";
                cantDelEq="Can't Remove this Equalizer Play Mode";
                acceptDialog="Accept";
                cancelDialog="Cancel";
                messageDeleted="Removed";
                removePlayModeEq="Remove Equalizer Play Mode";
                newSongs="New Songs";
                updatedSongs="Updated Songs";
                NewEqualizerText="New Equalizer";
                EqualizerTextTitle="Play Mode Name";
                CreatePlayModeText="Create";
                ModPlayModeText="Modify";
                invalidNamePlayMode="PlayMode Without Name";
                invalidNamePlayList="PlayList Without Name";
                createdPlayModeEq="Created";
                modPlayModeEq="Modified";
                MaintenanceNewSongsText="New and Updated Songs";
                MaintenanceNewSongsSubText="Recently Added or Updated Songs";
                MaintenanceLostSongsText="Lost Songs";
                MaintenanceLostSongsSubText="Songs Not Found by the File System";
                SettingsThemeText="Theme";
                SettingsThemeSubText="Change the Colors of the Application";
                SettingsLanguageText="Language";
                SettingsLanguageSubText="Change the Application Language";
                SettingsPlayerText="Player Options";
                SettingsPlayerSubText="Change the Player Options";
                LanguageESText="Spanish";
                LanguageESSubText="Change the Application Language to Spanish";
                LanguageENText="English";
                LanguageENSubText="Change the Application Language to English";
                ColorBackgroundPrimaryText="Main Background Color";
                ColorBackgroundPrimarySubText="Change the Background Color of the OptionBar and the Application Main Color";
                ColorBackgroundSecondaryText="Secondary Background Color";
                ColorBackgroundSecondarySubText="Change the Secondary Background Color of MediaPlayer";
                ColorPrimaryFirstTextText="Main Text Color of Main Objects";
                ColorPrimaryFirstTextSubText="Change the Main Text Color of the OptionBar";
                ColorPrimarySecondTextText="Secondary Text Color of Main Objects";
                ColorPrimarySecondTextSubText="Change the Secondary Text Color of the OptionBar";
                ColorSecondaryFirstTextText="Main Text Color of Secondary Objects";
                ColorSecondaryFirstTextSubText="Change the Main Text Color of the Elements such as List, Lyrics";
                ColorSecondarySecondTextText="Secondary Text Color of Secondary Objects";
                ColorSecondarySecondTextSubText="Change the Secondary Text Color of the Elements in a List";
                setColor="Change Color";
                lastDateMod="Last Modified: ";
                lostOptionsDialogTittle="Options";
                MaintenanceReplaceSongText="Replace Lost Song";
                MaintenanceReplaceSongSubText="Replace this Lost Song with Another in All PlayLists";
                MaintenanceRemoveSongText="Remove Lost Song";
                MaintenanceRemoveSongSubText="Remove the reference of a Lost Song in All PlayLists";
                newPlayListName="New PlayList";
                CreatePlayListText="Create PlayList";
                RenPlayListText="Rename PlayList";
                RenPlayListSubText="Change the Name of the PlayList";
                ModPlayListText="Modify PlayList";
                ModPlayListSubText="Add, Move and Remove Songs in the PlayList";
                RemPlayListText="Remove PlayList";
                RemPlayListSubText="Remove a Created PlayList";
                modPlayList="Renamed";
                removePlayList="Remove PlayList";
                replaceText="Replace: ";
                messageReplaced="Replaced";
                removeSong="Remove Lost Song";
                areYouSureReplace="Are You Sure to Replace:";
                areYouSureReplaceby="With:";
                BrowseArtistText="By Artist";
                BrowseArtistSubText="Search Songs by Artist";
                BrowseAlbumText="By Album";
                BrowseAlbumSubText="Search Songs by Album";
                BrowseGenreText="By Genre";
                BrowseGenreSubText="Search Songs by Genre";
                addSongsPlayList="Add Songs";
                ArtistTotalAlbum="Album(s)";
                GenreTotalSongs="Song(s)";
                PlayerSettingAutoStop="Pause Playback on Headset Disconnection";
                UserManualMediaPlayerText="Media Player";
                UserManualMediaPlayerSubText="About of Media Player";
                UserManualMediaPlayerContent="LexRis Logic 2017\n" +
                        "The media player receives a set of songs to be played, managed and controlled in " +
                        "order to extract information for the most played or favorite playlists.\n" +
                        "The playlist that the media player currently has is not updated by changes from the original playlist.\n"+
                        "The player has an option to pause playback if the headset is disconnected, " +
                        "you can find the option under setup\n" +
                        "When the player is reopened the data retrieved for the current playlist is:\n" +
                        "- The set of songs.\n" +
                        "- The title of the set of songs.\n" +
                        "- the last song played.\n" +
                        "- The position of the last song.\n" +
                        "- The selected equalizer playmode.\n" +
                        "You can save a copy of the player's database to be loaded in that state, so you can" +
                        " save all your playlists and equalizer playback modes.\n";

                UserManualSongManagementText="Songs Management";
                UserManualSongManagementSubText="About of Songs";
                UserManualSongManagementContent="When opening the application will read the new songs and their updates; " +
                        "A new song is one that has a new path and file name, and an updated song is one that has undergone " +
                        "some modification but still maintains the same path and filename.\n" +
                        "When a song is deleted in the file system, it will not be deleted in the player, but it will be a " +
                        "lost song, these songs can be displayed in the maintenance section to be able to choose one " +
                        "of two options:\n" +
                        "- Delete the song, thus updating all the playlists that contained it.\n" +
                        "- Replace the song with a valid song, replacing in all playlists the song lost by its replacement.\n" +
                        "Take into account if a lost song is found again using its path and filename, in other words, " +
                        "it appears again in the file system of the cell phone; Will automatically be replaced by updating " +
                        "your data, in case there is an update, and you don't need to do a manual replacement. " +
                        "You can abuse this option to manage the application as a music library.";
                UserManualPlayListText="PlayLists";
                UserManualPlayListSubText="About of PlayLists";
                UserManualPlayListContent="Playlists are created with indexes assigned to each song, so when you replace " +
                        "one song with another, in the maintenance part, it will automatically be replaced in playlists.";
                UserManualPlayModeText="Equalizer PlayModes";
                UserManualPlayModeSubText="About of Equalizer";
                UserManualPlayModeContent="Equalizer PlayModes can be selected on the player screen, the user can create more " +
                        "playmodes, if you want to create a playmode based on another, just select the playmode from which it " +
                        "will be based and then choose the option to create new playmode.";
                SongShareText="Share";
                SongShareSubText="Share Audio File";
                SongShareMessage="Share";
                SongRingtoneText="Set as Ringtone";
                SongRingtoneSubText="Set the Selected Song as Ringtone";
                SongRingtoneMessage="New Ringtone:\n";
                SongRemoveText="Delete Song";
                SongRemoveSubText="Delete the Song in File System";
                SongRemoveMessage="Are you Sure to Delete ";
                errorRemoveSong="You can't delete the currently playing song";
                cantRemoveSong="Error Deleting File";
                successRemoveSong=" Deleted";
                PlayerSettingSaveBackup="Save Media Player Info in File";
                PlayerSettingLoadBackup="Load Media Player Info from File";
                successBackup="Backup created in: ";
                errorBackup="Error Creating Backup File.";
                totalSongMessage="Total Songs: ";
                errorLoadingCloseMP="Turn Off the MediaPlayer before loading the Backup";
                errorLoadingFileMP="Backup File not Found in the phone memory: ";
                successRefreshBD="Information was Updated.";
                LoadBackupMessage="Load a Backup Delete All Actual Info, Do you wish to Continue?";
                PlayerSettingAutoReplay="Repeat Song with Previus Song Button";
                BackupText="Backup";
                pathText="File Path: ";
                anyPath="No Change";
                startBackup="Load Backup";
                replaceTextBackup="Change By:";
                break;
            default:
                break;
        }
    }
}
