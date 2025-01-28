import * as z from "zod";

export const createUserSchema = z.object({
  name: z.string().nonempty(),
  email: z.string().email().nonempty(),
  telephone: z.string().min(1),
  registeredAt: z.string().nonempty(),
});

export const updateUserSchema = createUserSchema.extend({
  id: z.number(),
});

export type CreateUserType = z.infer<typeof createUserSchema>;
export type User = z.infer<typeof updateUserSchema>;
