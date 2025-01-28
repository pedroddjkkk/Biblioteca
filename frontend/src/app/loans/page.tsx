"use client";

import { DataTable } from "@/components/data-table";
import { LoanDialog } from "@/components/loan-dialog";
import { Button } from "@/components/ui/button";
import { useToast } from "@/hooks/use-toast";
import { CreateLoanType, Loan, UpdateLoanType } from "@/models/loan";
import { loanService } from "@/services/loan";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ColumnDef } from "@tanstack/react-table";
import { AxiosError } from "axios";
import { Pen, Plus } from "lucide-react";
import { useState } from "react";
import { ErrorOption, FieldPath } from "react-hook-form";

export default function Loans() {
  const [loanDialogOpen, setLoanDialogOpen] = useState(false);
  const [selectedLoan, setSelectedLoan] = useState<Loan>();
  const [error, setError] = useState<{
    name: FieldPath<CreateLoanType>;
    error: ErrorOption;
  }>();
  const { toast } = useToast();
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ["loans"],
    queryFn: async () => loanService.getLoans(),
  });

  const createLoanMutation = useMutation({
    mutationFn: async (loan: CreateLoanType) => {
      await loanService.createLoan(loan);
    },
    onSuccess: () => {
      toast({ title: "Loan created successfully" });
      setLoanDialogOpen(false);
      queryClient.invalidateQueries({
        queryKey: ["loans"],
      });
    },
    onError(error) {
      if (error instanceof AxiosError && error.response) {
        setError({
          name: "bookId",
          error: { message: error.response.data.message },
        });
      } else {
        setError({
          name: "bookId",
          error: { message: "An unexpected error occurred" },
        });
      }
    },
  });

  const updateLoanMutation = useMutation({
    mutationFn: async (loan: UpdateLoanType) => {
      await loanService.updateLoan(loan);
    },
    onSuccess: () => {
      toast({ title: "Loan updated successfully" });
      setLoanDialogOpen(false);
      queryClient.invalidateQueries({
        queryKey: ["loans"],
      });
    },
  });

  const columns: ColumnDef<Loan>[] = [
    {
      accessorKey: "id",
      header: "#",
    },
    {
      accessorKey: "status",
      header: "Status",
    },
    {
      accessorKey: "devolutionDate",
      header: "Devolution Date",
      accessorFn: (row) => {
        //Fuso horário de Brasília
        row.devolutionDate.setHours(row.devolutionDate.getHours() + 3);
        return row.devolutionDate.toLocaleDateString("pt-BR");
      },
    },
    {
      accessorKey: "loanDate",
      header: "Loan Date",
      accessorFn: (row) => {
        //Fuso horário de Brasília
        row.loanDate.setHours(row.loanDate.getHours() + 3);
        return row.loanDate.toLocaleDateString("pt-BR");
      },
    },
    {
      accessorKey: "user",
      header: "User",
      accessorFn: (row) => {
        return row.user.name;
      },
    },
    {
      accessorKey: "book",
      header: "Book",
      accessorFn: (row) => {
        return row.book.title;
      },
    },
    {
      header: "Actions",
      cell: ({ row }) => {
        return (
          <Button
            variant="outline"
            size="icon"
            onClick={() => {
              setSelectedLoan(row.original);
              setLoanDialogOpen(true);
            }}
          >
            <Pen />
          </Button>
        );
      },
    },
  ];

  function handleCreateLoanClick() {
    setSelectedLoan(undefined);
    setLoanDialogOpen(true);
  }

  function handleCreateLoanSubmit(loan: CreateLoanType) {
    if (selectedLoan) {
      updateLoanMutation.mutate({ id: selectedLoan.id, ...loan });
    } else {
      createLoanMutation.mutate(loan);
    }
  }

  function mapLoanToCreateLoan(loan?: Loan) {
    if (!loan) return undefined;

    return {
      bookId: loan.book.id.toString(),
      loanDate: loan.loanDate,
      devolutionDate: loan.devolutionDate,
      status: loan.status,
      userId: loan.user.id.toString(),
    };
  }

  return (
    <div className="w-[90%] mx-auto flex flex-col gap-4">
      <Button
        className="self-end"
        variant={"outline"}
        onClick={handleCreateLoanClick}
      >
        <Plus /> Create Loan
      </Button>
      <DataTable columns={columns} data={data?.data ?? []} />
      <LoanDialog
        open={loanDialogOpen}
        loan={mapLoanToCreateLoan(selectedLoan)}
        onOpenChange={setLoanDialogOpen}
        onSubmit={handleCreateLoanSubmit}
        error={error}
      />
    </div>
  );
}
