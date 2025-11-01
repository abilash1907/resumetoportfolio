
import {styles} from "../style"

function NavBar({code}) {

    return (
        <header style={styles.header}>
            <span style={styles.title}>Resume to Portfolio</span>
            <button style={code?.html?styles.deployBtn:styles.disableDeployBtn} disabled={!code?.html}>Deploy</button>
        </header>
    )
}

export default NavBar;