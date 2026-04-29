package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.SelfGraphqlBookApi;
import com.knubisoft.testapi.model.graphql.SelfGraphqlBook;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@GraphQLApi
public class SelfGraphqlBookController implements SelfGraphqlBookApi {

    private final List<SelfGraphqlBook> storage = new CopyOnWriteArrayList<>();

    @Override
    @GraphQLQuery(name = "getSelfBookByTitle")
    public SelfGraphqlBook getSelfBookByTitle(@GraphQLArgument(name = "title") final String title) {
        for (final SelfGraphqlBook book : storage) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    @Override
    @GraphQLQuery(name = "getAllSelfBooks")
    public List<SelfGraphqlBook> getAllSelfBooks() {
        return new ArrayList<>(storage);
    }

    @Override
    @GraphQLMutation(name = "addSelfBook")
    public SelfGraphqlBook addSelfBook(@GraphQLArgument(name = "newBook") final SelfGraphqlBook book) {
        storage.add(book);
        return book;
    }

    @Override
    @GraphQLMutation(name = "updateSelfBookByTitle")
    public SelfGraphqlBook updateSelfBookByTitle(@GraphQLArgument(name = "title") final String title,
                                                 @GraphQLArgument(name = "updatedBook") final SelfGraphqlBook updatedBook) {
        for (final SelfGraphqlBook book : storage) {
            if (book.getTitle().equals(title)) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setNumber(updatedBook.getNumber());
                return book;
            }
        }
        return null;
    }

    @Override
    @GraphQLMutation(name = "deleteSelfBookByTitle")
    public boolean deleteSelfBookByTitle(@GraphQLArgument(name = "title") final String title) {
        for (final SelfGraphqlBook book : storage) {
            if (book.getTitle().equals(title)) {
                storage.remove(book);
                return true;
            }
        }
        return false;
    }

    @Override
    @GraphQLMutation(name = "resetSelfBooks")
    public boolean resetSelfBooks() {
        storage.clear();
        return true;
    }
}