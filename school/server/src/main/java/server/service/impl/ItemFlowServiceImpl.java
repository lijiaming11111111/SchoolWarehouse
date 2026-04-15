package server.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.item.flow.PageSelectItemFlowDTO;
import com.school.result.PageResult;
import com.school.vo.item.category.PageSelectItemCategoryVO;
import com.school.vo.item.flow.PageSelectItemFlowVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.ItemFlowMapper;
import server.service.ItemFlowService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemFlowServiceImpl implements ItemFlowService {

    private final ItemFlowMapper itemFlowMapper;

    @Override
    public PageResult<PageSelectItemFlowVO> pageSelectItemCategory(PageSelectItemFlowDTO pageSelectItemFlowDTO) {
        PageHelper.startPage(pageSelectItemFlowDTO.getPage(), pageSelectItemFlowDTO.getPageSize());
        Page<PageSelectItemFlowVO> page = itemFlowMapper.pageQueryItemFlow(pageSelectItemFlowDTO);
        return new PageResult<>( page.getTotal(), page.getResult());
    }
}
