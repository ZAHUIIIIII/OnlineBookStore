package com.onlinebookstore.services;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.structures.CustomArrayList;

public class BookService {
    private static final BookService instance = new BookService();
    private final CustomArrayList<Book> books = new CustomArrayList<>();
    private int nextId = 1;

    private BookService() {
        initializeBooks();
    }

    public static BookService getInstance() {
        return instance;
    }


    private void initializeBooks() {
        String[] titles = {
            "Bakuman", "Akira", "Dragon Ball: The Complete Edition", 
            "Naruto: The Official Fanbook", "One Piece: Color Walk Compendium", 
            "Attack on Titan: The Harsh Mistress of the City", "Death Note: How to Read", "Fullmetal Alchemist: The Complete Four-Panel Comics", 
            "My Hero Academia: School Briefs", "Sailor Moon Eternal Edition", 
            "Bleach: Official Character Book SOULs", "Tokyo Ghoul Illustrations: zakki", "Vagabond: Deluxe Edition", 
            "Hunter x Hunter: The Complete Guide", "Demon Slayer: Kimetsu no Yaiba Fanbook", 
            "JoJo's Bizarre Adventure: Art of JoJo", "One Punch Man: Hero's Notebook", "Fairy Tail: Master’s Edition", 
            "The Art of Studio Ghibli", "Attack on Titan Anthology"
        };
        String[] authors = {
            "Tsugumi Ohba & Takeshi Obata", "Katsuhiro Otomo", "Akira Toriyama", 
            "Masashi Kishimoto", "Eiichiro Oda", 
            "Hajime Isayama", "Tsugumi Ohba", "Hiromu Arakawa", 
            "Kohei Horikoshi", "Naoko Takeuchi", 
            "Tite Kubo", "Sui Ishida", "Takehiko Inoue", 
            "Yoshihiro Togashi", "Koyoharu Gotouge", 
            "Hirohiko Araki", "ONE", "Hiro Mashima", 
            "Hayao Miyazaki", "Hajime Isayama"
        };
        int[] quantities = {10, 15, 20, 5, 8, 12, 7, 9, 14, 6, 11, 13, 16, 4, 18, 3, 17, 19, 2, 1};
        double[] prices = {45.99, 55.99, 39.99, 49.99, 59.99, 35.99, 29.99, 65.99, 75.99, 85.99, 25.99, 45.99, 55.99, 65.99, 75.99, 85.99, 95.99, 105.99, 115.99, 125.99};
        for (int i = 0; i < titles.length; i++) {
            Book book = new Book(titles[i], authors[i], quantities[i], prices[i]);
            books.add(book);
        }
        sortBooks();
    }

    public void sortBooks() {
        // Sort books by ID to ensure they are in order
        books.mergeSort((Book b1, Book b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
        // Reset IDs after sorting
        nextId = 1;
        for (Book book : books) {
            book.setId(nextId++);
        }
    }

    public CustomArrayList<Book> getAllBooks() {
        return books;
    }
    
    public Book getBookById(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                return books.get(i);
            }
        }
        return null;
    }

    public void addBook(Book book) {
        book.setId(nextId++);
        books.add(book);
        sortBooks();
    }

    public void updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                sortBooks();
                return;
            }
        }
    }

    public void deleteBook(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
                return;
            }
        }
    }
}