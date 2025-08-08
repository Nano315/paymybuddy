export interface TransactionDTO {
  id: number;
  senderId: number;
  receiverId: number;
  senderUsername: string;
  receiverUsername: string;
  amount: number;
  commission: number;
  description: string | null;
  createdAt: string;
}
