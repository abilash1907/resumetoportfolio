import { useState } from 'react';
import NavBar from './components/NavBar';
import UploadResume from './components/UploadResume';
import Portfolio from './components/Portfolio';
import { styles } from './style';
const initialCode = {
  html: '',
  preview: '',
  content: ''
};

function App() {
  // const [file, setFile] = useState(null);
  const [code, setCode] = useState(initialCode);
  return (
    <div style={styles.container}>
      {/* Header */}
      <NavBar code={code}/>
      {/* Upload Option */}
      <UploadResume code={code} setCode={setCode}/>
      {/* Code Editor and Preview Section */}
      <Portfolio code={code} setCode={setCode}/>
    </div>
  );
}

export default App;
