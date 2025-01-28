import * as z from "zod";
import { User } from "./user";
import { Book } from "./book";

export const createLoanSchema = z.object({
  loanDate: z.date(),
  devolutionDate: z.date(),
  status: z.enum(["PENDING", "RETURNED"]),
  userId: z.string().nonempty(),
  bookId: z.string().nonempty(),
});

export const updateLoanSchema = z.object({
  id: z.number(),
  devolutionDate: z.date(),
  status: z.enum(["PENDING", "RETURNED"]),
});

export type CreateLoanType = z.infer<typeof createLoanSchema>;
export type UpdateLoanType = z.infer<typeof updateLoanSchema>;
export type Loan = {
  id: number;
  loanDate: Date;
  devolutionDate: Date;
  status: "PENDING" | "RETURNED";
  user: User;
  book: Book;
};
