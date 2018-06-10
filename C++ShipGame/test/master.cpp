#define BOOST_AUTO_TEST_MAIN//root of all tests suites and cases
#if defined(__GNUC__) || defined(__GNUG__)
#define BOOST_TEST_DYN_LINK //use shared boost library
#endif
#include <boost/test/unit_test.hpp>

using namespace boost::unit_test;

struct MyConfig
{

    MyConfig()
    {
        BOOST_TEST_MESSAGE("START");
        // unit_test_log.set_threshold_level(log_all_errors);

    }

    ~MyConfig()
    {
        BOOST_TEST_MESSAGE("END");

    }

};

BOOST_GLOBAL_FIXTURE(MyConfig);
