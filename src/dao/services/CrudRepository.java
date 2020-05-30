package dao.services;

public interface CrudRepository<T, ID> {
	
	T[] read(ID id);

	T create(T t);

	void update(ID id, T data);

	void delete(ID id);
}
