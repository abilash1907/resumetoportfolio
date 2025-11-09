import { useState } from 'react';
import NavBar from './components/NavBar';
import UploadResume from './components/UploadResume';
import Portfolio from './components/Portfolio';
import { styles } from './style';
const initialCode = {
  html: '',
  preview: '',
  content: '',
  json:{},
  domainRes:{}
};

function App() {
  const [file, setFile] = useState(null);
  const [code, setCode] = useState(initialCode);
  return (
    <div style={styles.container}>
      {/* Header */}
      <NavBar/>
      {/* Upload Option */}
      <UploadResume code={code} setCode={setCode} file={file} setFile={setFile}/>
      {/* Code Editor and Preview Section */}
      <Portfolio code={code} setCode={setCode} setFile={setFile}/>
    </div>
  );
}

export default App;
