package com.cnsworder.transgo.data;

import java.util.List;

/**
 * 数据库操作接口
 * @author 伊冲
 * @version 1.0
 * 2010-5-24
 */
public interface IData<T> {

	//public  T query(int id);
	public List<T> query();
	public boolean update(T info);
	//public boolean insert(T info);
	//public boolean delete(T info);
	public boolean clear();
}
