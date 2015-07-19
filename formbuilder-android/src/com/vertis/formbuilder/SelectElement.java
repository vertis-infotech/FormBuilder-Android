package com.vertis.formbuilder;

public class SelectElement implements Comparable<SelectElement> {
	@Override
	public String toString() {
		return value;
	}

	String key;
	String value;
	Integer position;
	int sortType;

	SelectElement(Integer position, String key, String value) {
		this(position, key, value, 0);
	}

	SelectElement(Integer position, String key, String value, int sortType) {
		this.position = position;
		this.key = key;
		this.value = value;
		this.sortType = sortType;
	}

	@Override
	public int compareTo(SelectElement another) {
		if (sortType == 0) {
			return position.compareTo(another.position);
		} else {
			return value.compareTo(another.value);
		}
	}

	@Override
	public boolean equals(Object o) {
		SelectElement element = (SelectElement) o;
		if (value.equals(element.value)) {
			return true;
		}
		return false;
	}
}