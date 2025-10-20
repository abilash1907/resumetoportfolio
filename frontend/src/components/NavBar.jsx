
import {styles} from "../style"

function NavBar() {

    return (
        <header style={styles.header}>
            <span style={styles.title}>Resume to Portfolio</span>
            <button style={styles.deployBtn}>Deploy</button>
        </header>
    )
}

export default NavBar;