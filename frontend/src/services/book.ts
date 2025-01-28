import { http } from "@/lib/http";
import { Book, CreateBookType } from "@/models/book";

class BookService {
  async getAllBooks() {
    const { data } = await http.get<Book[]>("/books");
    const books = data.map((book) => ({
      ...book,
      publishedAt: new Date(book.publishedAt),
    }));
    return { data: books };
  }

  async deleteBook(id: number) {
    return await http.delete(`/books/${id}`);
  }

  async createBook(book: CreateBookType) {
    return await http.post("/books", book);
  }

  async updateBook(book: Book) {
    return await http.put("/books", book);
  }
}

export const bookService = new BookService();
