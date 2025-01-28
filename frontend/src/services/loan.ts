import { http } from "@/lib/http";
import { CreateLoanType, Loan, UpdateLoanType } from "@/models/loan";

class LoanService {
  async getLoans() {
    const { data } = await http.get<Loan[]>("/loans");
    const loans = data.map((loan) => ({
      ...loan,
      devolutionDate: new Date(loan.devolutionDate),
      loanDate: new Date(loan.loanDate),
    }));
    return { data: loans };
  }

  async createLoan(loan: CreateLoanType) {
    return await http.post("/loans", loan);
  }

  async updateLoan(loan: UpdateLoanType) {
    return await http.put(`/loans`, loan);
  }
}

export const loanService = new LoanService();
