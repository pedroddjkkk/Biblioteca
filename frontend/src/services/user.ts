import { http } from "@/lib/http";
import { Book } from "@/models/book";
import { CreateUserType, User } from "@/models/user";

class UserService {
  async getAllUsers() {
    return await http.get<User[]>("/users");
  }

  async deleteUser(id: number) {
    return await http.delete(`/users/${id}`);
  }

  async createUser(user: CreateUserType) {
    return await http.post("/users", user);
  }

  async updateUser(user: User) {
    return await http.put("/users", user);
  }

  async findRecommendations(userId: number) {
    return await http.get<Book[]>(`/books/recommendation/${userId}`);
  }
}

export const userService = new UserService();
