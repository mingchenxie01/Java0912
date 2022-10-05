package Homework.day1004;
import java.util.*;

public class Homework1004 {
    public static void main(String[] args) {

        SongCacheImpl songCacheImpl = new SongCacheImpl();
        songCacheImpl.recordSongPlays("ID-1", 3);
        songCacheImpl.recordSongPlays("ID-1", 1);
        songCacheImpl.recordSongPlays("ID-2", 2);
        songCacheImpl.recordSongPlays("ID-3", 5);
        // Expecting to see 4 times played when the songId is "ID-1"
        System.out.println(songCacheImpl.getPlaysForSong("ID-1"));

        // Expecting to see -1 when the songId is "ID-9, since ID-9 is not a valid ID"
        System.out.println(songCacheImpl.getPlaysForSong("ID-9"));

        // Expecting to see [ID-3, ID-1] since ID-3 has been played 5 times, and ID-1 has been played 4 times
        System.out.println(songCacheImpl.getTopNSongsPlayed(2));

        // Expecting to see an empty list.
        System.out.println(songCacheImpl.getTopNSongsPlayed(0));
    }
}
class SongCacheImpl implements SongCache{

    private Map<String, Integer> map;
    public SongCacheImpl(){
         map = new HashMap<>();
    }

    @Override
    synchronized public void recordSongPlays(String songId, int numPlays) {
        map.put(songId, map.getOrDefault(songId, 0) + numPlays);
    }

    @Override
    synchronized public int getPlaysForSong(String songId) {
        return map.getOrDefault(songId, -1);
    }

    @Override
    synchronized public List<String> getTopNSongsPlayed(int n) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        for(Map.Entry<String, Integer> m: map.entrySet()){
            list.add(m);
        }
        Collections.sort(list, (x, y) ->
                {
                    return y.getValue() - x.getValue();
                });
        List<String> res = new ArrayList<>();
        for(int i = 0; i < n; i++){
            Map.Entry<String, Integer> temp = list.get(i);
            res.add(temp.getKey());
        }
        return res;
    }
}



