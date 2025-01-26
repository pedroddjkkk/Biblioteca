"use client";

import { DataTable } from "@/components/data-table";
import { User } from "@/types/user";
import { ColumnDef } from "@tanstack/react-table";

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
];

export default function Users() {
  return (
    <div className="w-[90%] mx-auto">
      <DataTable columns={columns} data={[]} />
    </div>
  );
}
