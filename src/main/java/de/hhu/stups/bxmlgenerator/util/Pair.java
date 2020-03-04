package de.hhu.stups.bxmlgenerator.util;


import java.io.Serializable;

public class Pair<K, V> implements Serializable {
    private K key;
    private V value;

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }

    public int hashCode() {
        return Long.valueOf(this.key.hashCode() + this.value.hashCode()).hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else{
            if (!(o instanceof Pair)) {
                return false;
            } else {

                Pair pair = (Pair) o;

                if (this.key != null) {
                    if (!this.key.equals(pair.key)) {
                        return false;
                    }
                } else {
                    if (pair.key != null) {
                        return false;
                    }
                }

                if (this.value != null) {
                    return this.value.equals(pair.value);
                } else {
                    return pair.value == null;
                }

            }
        }
    }
}
