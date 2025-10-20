import { Box, Button, Tab } from "@mui/material";
import { styles } from "../style";
import React, { useState } from 'react';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
export default function Portfolio({ code, setCode }) {
  const [previewSrc, setPreviewSrc] = useState('');
  const [currentTab, setCurrentTab] = useState('content');

  React.useEffect(() => {
    const srcDoc = `${code?.html}`;
    setPreviewSrc(srcDoc);
  }, [code]);

  const handleCodeChange = (type, e) => {
    if (e.type === "click") {
      setCurrentTab(type);
    } else {
      setCode({ ...code, [type]: e.target.value });
    }
  };
 const openInNewTab = () => {
    const blob = new Blob([previewSrc], { type: "text/html" });
    const url = URL.createObjectURL(blob);
    window.open(url, "_blank");
  };
  return (
    <Box sx={{ width: '100%', typography: 'body1' }}>
      <TabContext value={currentTab}>
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
          <TabList onChange={(e, type) => handleCodeChange(type, e)} aria-label="lab API tabs example">
            <Tab label="Content" value="content" />
            <Tab label="Preview" value="preview" />
            <Tab label="Code" value="code" />
          </TabList>
        </Box>
        <TabPanel value="content">
          {/* <pre style={{ background: '#f5f5f7', padding: 16, borderRadius: 6 }}>
            {JSON.stringify(code?.json, null, 2)}
          </pre> */}
          <textarea
            rows={6}
            placeholder="File Content"
            value={code.content}
            onChange={(e) => handleCodeChange('content', e)}
            style={styles.textArea}
          />
        </TabPanel>
        <TabPanel value="code">
          <textarea
            rows={6}
            placeholder="HTML code"
            value={code.html}
            onChange={(e) => handleCodeChange('html', e)}
            style={styles.textArea}
          />
        </TabPanel>
        <TabPanel value="preview">
          <Button onClick={openInNewTab}>Open Preview in New Tab</Button>
          <iframe
            title="Website Preview"
            srcDoc={previewSrc}
            style={styles.iframe}
            sandbox="allow-scripts allow-same-origin"
          />
        </TabPanel>
      </TabContext>
    </Box>
  );


}

