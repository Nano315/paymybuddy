export interface CreateTransactionRequest {
  senderId: number;
  receiverEmail: string;
  amount: number;
  description?: string;
}
