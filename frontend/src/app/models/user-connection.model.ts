import { UserConnectionKey } from './user-connection-key.model';
import { UserDTO } from './user.dto';

export interface UserConnection {
  id: UserConnectionKey;
  user: UserDTO;
  connection: UserDTO;
  createdAt: string;
}
