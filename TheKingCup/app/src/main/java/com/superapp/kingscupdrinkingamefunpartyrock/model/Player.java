package com.superapp.kingscupdrinkingamefunpartyrock.model;

/**
 * Created by ManhNV on 4/11/2016.
 */
public class Player {
    private int id;
   private String name;

    public Player(int id,String name) {
        this.id=id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
