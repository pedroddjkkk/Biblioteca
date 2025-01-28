import * as z from "zod";

export const createBookSchema = z.object({
  title: z.string().nonempty(),
  author: z.string().nonempty(),
  isbn: z.string().nonempty(),
  category: z.string().nonempty(),
  publishedAt: z.date(),
});

export const updateBookSchema = createBookSchema.extend({
  id: z.number(),
});

export type CreateBookType = z.infer<typeof createBookSchema>;
export type Book = z.infer<typeof updateBookSchema>;
