class SongRepository(private val songDao: SongDao) {

    val allSongs: LiveData<List<SongEntity>> = songDao.getAllSongs()

    suspend fun insert(song: SongEntity) = songDao.insert(song)
    suspend fun update(song: SongEntity) = songDao.update(song)
    suspend fun delete(song: SongEntity) = songDao.delete(song)
}
