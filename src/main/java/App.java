package main.java;

import main.java.com.app.shop.Shop;

public class App {
    public static void main(String[] args) throws Exception {
        
        //Crea una instancia de shop que a su vez corre el menu principal
        Shop cozyStore = new Shop();
        cozyStore.menu();
    }
}
