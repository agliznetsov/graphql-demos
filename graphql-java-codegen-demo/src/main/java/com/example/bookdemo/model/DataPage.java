package com.example.bookdemo.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataPage<T> {
	List<T> data;
	boolean nextPageExists;
	boolean reversed;

	public T getFirst() {
		return data.size() > 0 ? data.get(0) : null;
	}

	public T getLast() {
		return data.size() > 0 ? data.get(data.size() - 1) : null;
	}
}
