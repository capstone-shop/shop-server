package com.capstone.shop.core.domain.repository.merchandise;

import static com.capstone.shop.entity.QMerchandise.merchandise;
import static com.capstone.shop.entity.QCategory.category;

import com.capstone.shop.core.domain.entity.Category;
import com.capstone.shop.core.domain.entity.Merchandise;
import com.capstone.shop.entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MerchandiseQueryRepositoryImpl implements MerchandiseQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Merchandise> findRelatedMerchandises(Merchandise entity) {
        List<Category> categorieList = entity.getCategory().getCategoryList();
        List<Merchandise> result = new ArrayList<>();

        int relatedMerchandise = 6;
        for (int i = categorieList.size() - 1; i >= 0 && relatedMerchandise > 0; i--) {
            QCategory children = new QCategory("children");
            QCategory children2 = new QCategory("children2");
            Long currentCategoryId = categorieList.get(i).getId();

            List<Long> categoryIdList = queryFactory
                    .select(category.id, children.id, children2.id)
                    .from(category)
                    .leftJoin(category.children, children)
                    .leftJoin(children.children, children2)
                    .where(category.id.eq(currentCategoryId))
                    .fetch()
                    .stream()
                    .map(tuple -> {
                        if (tuple.get(children2.id) != null)
                            return tuple.get(children2.id);
                        if (tuple.get(children.id) != null)
                            return tuple.get(children.id);
                        return tuple.get(category.id);
                    })
                    .toList();

            List<Merchandise> merchandiseList = queryFactory
                    .select(merchandise)
                    .from(merchandise)
                    .leftJoin(category)
                    .on(category.id.in(categoryIdList))
                    .where(merchandise.id.ne(entity.getId()))
                    .orderBy(merchandise.wish.desc())
                    .limit(relatedMerchandise)
                    .fetch();

            result.addAll(merchandiseList);
            relatedMerchandise -= merchandiseList.size();
        }
        return result;
    }
}
