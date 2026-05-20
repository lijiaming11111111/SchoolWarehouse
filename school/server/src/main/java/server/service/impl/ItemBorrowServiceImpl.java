package server.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.item.borrow.PageSelectItemBorrowDTO;
import com.school.result.PageResult;
import com.school.vo.item.borrow.PageSelectItemBorrowVO;
import com.school.vo.item.flow.PageSelectItemFlowVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.ItemBorrowMapper;
import server.service.ItemBorrowService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemBorrowServiceImpl implements ItemBorrowService {

    private final ItemBorrowMapper itemBorrowMapper;

    @Override
    public PageResult<PageSelectItemBorrowVO> pageSelectItemBorrow(PageSelectItemBorrowDTO pageSelectItemBorrowDTO) {
        PageHelper.startPage(pageSelectItemBorrowDTO.getPage(), pageSelectItemBorrowDTO.getPageSize());
        Page<PageSelectItemBorrowVO> page = itemBorrowMapper.pageQueryItemBorrow(pageSelectItemBorrowDTO);
        return new PageResult<>( page.getTotal(), page.getResult());
    }
}
