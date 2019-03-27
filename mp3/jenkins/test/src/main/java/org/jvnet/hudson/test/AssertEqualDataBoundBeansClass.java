import static org.junit.Assert.*;

public class AssertEqualDataBoundBeans {
    public AssertEqualDataBoundBeans(){

    }

    public static void assertEqualDataBoundBeans(Object lhs, Object rhs) throws Exception {
        if (lhs==null && rhs==null)     return;
        if (lhs==null)      fail("lhs is null while rhs="+rhs);
        if (rhs==null)      fail("rhs is null while lhs="+lhs);
        
        Constructor<?> lc = findDataBoundConstructor(lhs.getClass());
        Constructor<?> rc = findDataBoundConstructor(rhs.getClass());
        assertEquals("Data bound constructor mismatch. Different type?",lc,rc);

        List<String> primitiveProperties = new ArrayList<String>();

        String[] names = ClassDescriptor.loadParameterNames(lc);
        Class<?>[] types = lc.getParameterTypes();
        assertEquals(names.length,types.length);
        for (int i=0; i<types.length; i++) {
            Object lv = ReflectionUtils.getPublicProperty(lhs, names[i]);
            Object rv = ReflectionUtils.getPublicProperty(rhs, names[i]);

            if (Iterable.class.isAssignableFrom(types[i])) {
                Iterable lcol = (Iterable) lv;
                Iterable rcol = (Iterable) rv;
                Iterator ltr,rtr;
                for (ltr=lcol.iterator(), rtr=rcol.iterator(); ltr.hasNext() && rtr.hasNext();) {
                    Object litem = ltr.next();
                    Object ritem = rtr.next();

                    if (findDataBoundConstructor(litem.getClass())!=null) {
                        assertEqualDataBoundBeans(litem,ritem);
                    } else {
                        assertEquals(litem,ritem);
                    }
                }
                assertFalse("collection size mismatch between "+lhs+" and "+rhs, ltr.hasNext() ^ rtr.hasNext());
            } else
            if (findDataBoundConstructor(types[i])!=null || (lv!=null && findDataBoundConstructor(lv.getClass())!=null) || (rv!=null && findDataBoundConstructor(rv.getClass())!=null)) {
                // recurse into nested databound objects
                assertEqualDataBoundBeans(lv,rv);
            } else {
                primitiveProperties.add(names[i]);
            }
        }

        // compare shallow primitive properties
        if (!primitiveProperties.isEmpty())
            assertEqualBeans(lhs,rhs,Util.join(primitiveProperties,","));
    }
}