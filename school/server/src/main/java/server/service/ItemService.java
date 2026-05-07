package server.service;

import com.school.dto.item.*;
import com.school.result.PageResult;
import com.school.vo.item.SelectItemVO;

public interface ItemService {
    String insertItem(InsertItemDTO insertItemDTO);

    Boolean updateItem(UpdateItemDTO updateItemDTO);

    Boolean deleteItem(String id);

    SelectItemVO selectItem(String id);

    Boolean updateItemStatus(UpdateItemStatusDTO updateItemStatusDTO);

    PageResult<SelectItemVO> pageSelectItem(PageSelectItemDTO pageSelectItemDTO);

    Boolean putItem(PutItemDTO putItemDTO);

    Boolean stockItem(StockItemDTO stockItemDTO);

    Boolean borrowItem(BorrowItemDTO borrowItemDTO);

    Boolean returnItem(String id);
}
