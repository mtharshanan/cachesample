package cachesample;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class MainCaching {

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		CacheLoader<String, Person> loader = new CacheLoader<String, Person>() {
			@Override
			public Person load(String key) throws Exception {
				return new Person("Person_" + key, 30);
			}
		};

		LoadingCache<String, Person> cache;
		//Max cache size
		cache = CacheBuilder.newBuilder().maximumSize(3).build(loader);
		loadAndPrint(cache);
		System.out.println("Last 3 cache are present above .....");
		
		//Expires after 5 seconds
		cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build(loader);
		loadAndPrint(cache);
		System.out.println("Thread sleeping for 5 sec .....");
		Thread.sleep(5001);
		print(cache);
		
		//Cache Eviction
		cache = CacheBuilder.newBuilder().build(loader);
		loadAndPrint(cache);
		System.out.println("cache size before eviction: "+cache.size());
		cache.invalidate("1");
		print(cache);
		System.out.println("cache size: "+cache.size());
		
		//Cache without loader
		Cache<String, Person> cacheNoLoader = CacheBuilder.newBuilder().build();
		cacheNoLoader.put("100", new Person("Peter", 39));
		System.out.println("\nCache without cache loader: "+cacheNoLoader.getIfPresent("100"));

	}

	private static void loadAndPrint(LoadingCache<String, Person> cache) {
		load(cache);
		print(cache);
	}

	private static void print(LoadingCache<String, Person> cache) {
		for (int i = 1; i < 10; i++) {
			System.out.format("Cache value after evition %s\n", cache.getIfPresent(String.valueOf(i)));
		}
	}

	private static void load(LoadingCache<String, Person> cache) {
		for (int i = 1; i < 10; i++) {
			cache.getUnchecked(String.valueOf(i));
			System.out.format("Cache value is %s\n", cache.getIfPresent(String.valueOf(i)));
		}
	}
}
