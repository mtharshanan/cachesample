package cachesample;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
		
		//Expires after 10 seconds
		cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build(loader);
		loadAndPrint(cache);
		Thread.sleep(10000);
		print(cache);

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
