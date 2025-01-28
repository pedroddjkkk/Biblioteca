"use client";

import { DataTable } from "@/components/data-table";
import { Button } from "@/components/ui/button";
import { UserDialog } from "@/components/user-dialog";
import { useToast } from "@/hooks/use-toast";
import { Book } from "@/models/book";
import { CreateUserType, User } from "@/models/user";
import { userService } from "@/services/user";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ColumnDef } from "@tanstack/react-table";
import { Pen, Plus, Trash } from "lucide-react";
import { useState } from "react";

export default function Users() {
  useState<User>();
  const [userDialogOpen, setUserDialogOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User>();
  const [recommendations, setRecommendations] = useState<Book[]>();
  const { toast } = useToast();
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ["users"],
    queryFn: async () => userService.getAllUsers(),
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: number) => {
      await userService.deleteUser(id);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["users"],
      });
    },
  });

  const createUserMutation = useMutation({
    mutationFn: async (user: CreateUserType) => {
      await userService.createUser(user);
    },
    onSuccess: () => {
      toast({ title: "User created successfully" });
      setUserDialogOpen(false);
      queryClient.invalidateQueries({
        queryKey: ["users"],
      });
    },
  });

  const updateUserMutation = useMutation({
    mutationFn: async (user: User) => {
      await userService.updateUser(user);
    },
    onSuccess: () => {
      toast({ title: "User updated successfully" });
      setUserDialogOpen(false);
      queryClient.invalidateQueries({
        queryKey: ["users"],
      });
    },
  });

  const columns: ColumnDef<User>[] = [
    {
      accessorKey: "id",
      header: "#",
    },
    {
      accessorKey: "name",
      header: "Name",
    },
    {
      accessorKey: "email",
      header: "Email",
    },
    {
      accessorKey: "telephone",
      header: "Telephone",
    },
    {
      accessorKey: "registeredAt",
      header: "Registered at",
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
                setSelectedUser(row.original);
                setUserDialogOpen(true);
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

  const recommendationColumns: ColumnDef<Book>[] = [
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
  ];

  function handleCreateUserClick() {
    setSelectedUser(undefined);
    setUserDialogOpen(true);
  }

  function handleCreateUserSubmit(user: User) {
    if (selectedUser) {
      updateUserMutation.mutate(user);
    } else {
      createUserMutation.mutate(user);
    }
  }

  async function handleSelectRecommendationUser(user: User) {
    const response = await userService.findRecommendations(user.id);
    setRecommendations(response.data);
  }

  return (
    <div className="w-[90%] mx-auto flex flex-col gap-4">
      <Button
        className="self-end"
        variant={"outline"}
        onClick={handleCreateUserClick}
      >
        <Plus /> Create User
      </Button>
      <DataTable
        columns={columns}
        data={data?.data ?? []}
        onRowClick={(row) => handleSelectRecommendationUser(row.original)}
      />
      <h1 className="scroll-m-20 text-xl font-normal tracking-tight lg:text-5xl mt-4">
        Book recomendations
      </h1>
      <DataTable
        columns={recommendationColumns}
        data={recommendations ?? []}
        noDataText={"Select an user first to see book recommendation"}
      />
      <UserDialog
        open={userDialogOpen}
        user={selectedUser}
        onOpenChange={setUserDialogOpen}
        onSubmit={handleCreateUserSubmit}
      />
    </div>
  );
}
