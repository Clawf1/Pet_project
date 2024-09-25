package com.clf;

import com.clf.dao.CatDao;
import com.clf.model.Cat;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CatDao dao = new CatDao();

        Cat cat1 = new Cat();
        cat1.setName("1");
        Cat cat2 = new Cat();
        cat2.setName("2");

        dao.save(cat1);
        dao.save(cat2);

        cat1.setFriends(List.of(cat2));
        cat2.setFriends(List.of(cat1));

        dao.update(cat1);
        dao.update(cat2);
    }
}
