package com.baeldung.lss.persistence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.baeldung.lss.model.User;

public class InMemoryUserRepository {

    private static AtomicLong counter = new AtomicLong();

    private final ConcurrentMap<Long, User> users = new ConcurrentHashMap<Long, User>();

    public Iterable<User> findAll() {
        return this.users.values();
    }

    public User save(User user) {
        Long id = user.getId();
        if (id == null) {
            id = counter.incrementAndGet();
            user.setId(id);
        }
        this.users.put(id, user);
        return user;
    }

    public User findUser(Long id) {
        return this.users.get(id);
    }

    public void deleteUser(Long id) {
        this.users.remove(id);
    }

}
