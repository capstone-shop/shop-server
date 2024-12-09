package com.capstone.shop.core.domain.repository.merchandise;

import com.capstone.shop.core.domain.entity.Merchandise;
import com.capstone.shop.user.v1.search.Filter;
import com.capstone.shop.user.v1.search.FilterOption;
import com.capstone.shop.user.v1.search.FilterType;
import com.capstone.shop.user.v1.search.QueryType;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class MerchandiseSpec {

    private Specification<Merchandise> spec;

    private MerchandiseSpec() {
        spec = (root, query, builder) -> null;
    }

    private <T> Path<T> getPathFromColumnName(Root<Merchandise> root, String columnName) {
        //컬럼 값에 . 이 포함되어 있으면 조인으로 처리
        String[] columns = columnName.split("\\.");

        From<?, ?> join = root;
        for (int i = 0; i < columns.length-1; i++)
            join = join.join(columns[i]);
        return join.get(columns[columns.length - 1]);
    }

    public MerchandiseSpec isOnSale() {
        spec = spec.and((root, query, builder) -> builder.equal(root.get("saleState"), "SALE"));
        return this;
    }

    //Filter 관련 메소드를 이해하기 전에 Filter 클래스부터 봐야 함.
    public MerchandiseSpec addFilterCriteria(Filter filter) {
        for (FilterType filterType : FilterType.values()) {
            List<FilterOption> options = filter.getFilterOptions(filterType);
            if (options != null)
                spec = spec.and(addFilter(filterType, options));
        }
        return this;
    }

    private Specification<Merchandise> addFilter(FilterType filterType, List<FilterOption> options) {
        return (root, query, builder) -> {
            String column = filterType.getColumnName();
            QueryType queryType = filterType.getQuery();

            if (queryType == QueryType.EQUAL){
                Path<Object> path = getPathFromColumnName(root, column);
                return options.stream()
                        .map(option -> builder.equal(path, option.getValue()))
                        .reduce(builder::or)
                        .orElse(null);
            }

            Path<Integer> path = getPathFromColumnName(root, column);
            return options.stream()
                    .map(option -> builder.greaterThanOrEqualTo(path, option.getIntValue()))
                    .reduce(builder::or)
                    .orElse(null);
        };
    }

    //검색 (카테고리명, 상품명을 기준으로 검색)
    public MerchandiseSpec addSearchString(String search) {
        if (search == null || search.isEmpty())
            return this;

        spec = spec.and((root, query, builder) -> {
            Join<?,?> cate = root.join("category", JoinType.LEFT);
            Join<?,?> catePa = cate.join("parent", JoinType.LEFT);
            Join<?,?> catePaPa = catePa.join("parent", JoinType.LEFT);

            List<Path<String>> paths = new ArrayList<>();
            paths.add(root.get("name"));
            paths.add(cate.get("title"));
            paths.add(catePa.get("title"));
            paths.add(catePaPa.get("title"));

            return paths.stream()
                    .map(path -> builder.like(path, "%" + search + "%"))
                    .reduce(builder::or)
                    .orElse(null);
        });

        return this;
    }

    public static MerchandiseSpec builder() {
        return new MerchandiseSpec();
    }

    public Specification<Merchandise> build() {
        return spec;
    }
}
