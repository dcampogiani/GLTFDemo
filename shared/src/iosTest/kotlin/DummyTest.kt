import com.danielecampogiani.gltfdemo.shared.Platform
import kotlin.test.Test
import kotlin.test.assertTrue

class DummyTest {

    @Test
    fun platformName() {
        assertTrue { Platform().platform.startsWith("iOS ") }
    }
}