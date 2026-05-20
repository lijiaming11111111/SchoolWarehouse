package server.service;

import com.school.dto.item.borrow.PageSelectItemBorrowDTO;
import com.school.result.PageResult;
import com.school.vo.item.borrow.PageSelectItemBorrowVO;

public interface ItemBorrowService {

    PageResult<PageSelectItemBorrowVO> pageSelectItemBorrow(PageSelectItemBorrowDTO pageSelectItemBorrowDTO);
}
