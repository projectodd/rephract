package org.projectodd.rephract.mop.java;

import org.junit.Before;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ReturnFiltersTest {
    public static int listToInt(List list) {
        return list.size();
    }

    public static List list() {
        return new ArrayList();
    }

    private MethodHandle listToInt;
    private MethodHandle list;

    @Before
    public void before() throws Exception {
        listToInt = MethodHandles.lookup().findStatic(ReturnFiltersTest.class, "listToInt", MethodType.methodType(int.class, List.class));
        list = MethodHandles.lookup().findStatic(ReturnFiltersTest.class, "list", MethodType.methodType(List.class));
    }

    @Test
    public void shouldHaveFilterWhenReturnTypeMatchesRegisteredCoercion_byMethodHandle() {
        ReturnFilters filters = new ReturnFilters();
        filters.addReturnFilter(List.class, listToInt);

        assertThat(filters.getReturnFilter(list)).isEqualTo(listToInt);
    }

    @Test
    public void shouldHaveFilterWhenReturnTypeMatchesRegisteredCoercion_byReturnType() {
        ReturnFilters filters = new ReturnFilters();
        filters.addReturnFilter(List.class, listToInt);

        assertThat(filters.getReturnFilter(List.class)).isEqualTo(listToInt);
    }

    @Test
    public void shouldHaveFilterWhenReturnTypeInterfaceMatchesRegisteredCoercion() {
        ReturnFilters filters = new ReturnFilters();
        filters.addReturnFilter(List.class, listToInt);

        assertThat(filters.getReturnFilter(ArrayList.class)).isEqualTo(listToInt);
    }

    @Test
    public void shouldInferIdentityFilterWhenNoCoercionAvailable() {
        ReturnFilters filters = new ReturnFilters();

        MethodHandle returnFilter = filters.getReturnFilter(ArrayList.class);

        assertThat(returnFilter.type()).isEqualTo(MethodType.methodType(ArrayList.class, ArrayList.class));
    }

    @Test
    public void shouldInferNoOpFilterForVoidReturnType() {
        ReturnFilters filters = new ReturnFilters();

        MethodHandle returnFilter = filters.getReturnFilter(void.class);

        assertThat(returnFilter).isEqualTo(filters.getNoOp());
    }
}
