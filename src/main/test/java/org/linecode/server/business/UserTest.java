package org.linecode.server.business;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User admin= new User("Valton",true);
    private User notAdmin= new User("Achimetto",false);

    @Test
    public void testGetUsername() {
        assertEquals("Valton", admin.getUsername());
    }

    @Test
    public void testIsAdmin() {
    }
}
