export interface TransactionDTO {
  id: number;
  senderId: number;
  receiverId: number;
  amount: number;
  commission: number;
  description: string | null;
  createdAt: string;
}
