"use client";

import { BookDialog } from "@/components/book-dialog";
import { DataTable } from "@/components/data-table";
import { Button } from "@/components/ui/button";
import { useToast } from "@/hooks/use-toast";
import { Book, CreateBookType } from "@/models/book";
import { bookService } from "@/services/book";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ColumnDef } from "@tanstack/react-table";
import { Pen, Plus, Trash } from "lucide-react";
import { useState } from "react";

export default function Books() {
  const [bookDialogOpen, setBookDialogOpen] = useState(false);
  const [selectedBook, setSelectedBook] = useState<Book>();
  const { toast } = useToast();
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ["books"],
    queryFn: async () => bookService.getAllBooks(),
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: number) => {
      await bookService.deleteBook(id);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["books"],
      });
    },
  });

  const createBookMutation = useMutation({
    mutationFn: async (book: CreateBookType) => {
      await bookService.createBook(book);
    },
    onSuccess: () => {
      toast({ title: "Book created successfully" });
      setBookDialogOpen(false);
      queryClient.invalidateQueries({
        queryKey: ["books"],
      });
    },
  });

  const updateBookMutation = useMutation({
    mutationFn: async (book: Book) => {
      await bookService.updateBook(book);
    },
    onSuccess: () => {
      toast({ title: "Book updated successfully" });
      setBookDialogOpen(false);
      queryClient.invalidateQueries({
        queryKey: ["books"],
      });
    },
  });

  const columns: ColumnDef<Book>[] = [
    {
      accessorKey: "id",
      header: "#",
    },
    {
      accessorKey: "title",
      header: "Title",
    },
    {
      accessorKey: "author",
      header: "Author",
    },
    {
      accessorKey: "isbn",
      header: "ISBN",
    },
    {
      accessorKey: "category",
      header: "Category",
    },
    {
      accessorKey: "publishedAt",
      header: "Published at",
      accessorFn: (row) => {
        //Fuso horário de Brasília
        row.publishedAt.setHours(row.publishedAt.getHours() + 3);
        return row.publishedAt.toLocaleDateString("pt-BR");
      },
    },
    {
      header: "Actions",
      cell: ({ row }) => {
        return (
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="icon"
              onClick={() => {
                setSelectedBook(row.original);
                setBookDialogOpen(true);
              }}
            >
              <Pen />
            </Button>
            <Button
              variant="destructive"
              size="icon"
              onClick={() => deleteMutation.mutate(row.original.id)}
            >
              <Trash />
            </Button>
          </div>
        );
      },
    },
  ];

  function handleCreateBookClick() {
    setSelectedBook(undefined);
    setBookDialogOpen(true);
  }

  function handleCreateBookSubmit(book: Book) {
    if (selectedBook) {
      updateBookMutation.mutate(book);
    } else {
      createBookMutation.mutate(book);
    }
  }

  return (
    <div className="w-[90%] mx-auto flex flex-col gap-4">
      <Button
        className="self-end"
        variant={"outline"}
        onClick={handleCreateBookClick}
      >
        <Plus /> Create Book
      </Button>
      <DataTable columns={columns} data={data?.data ?? []} />
      <BookDialog
        open={bookDialogOpen}
        book={selectedBook}
        onOpenChange={setBookDialogOpen}
        onSubmit={handleCreateBookSubmit}
      />
    </div>
  );
}
