import { Editor } from "@monaco-editor/react";
import CodeIcon from '@mui/icons-material/Code';
// import DescriptionIcon from '@mui/icons-material/Description';
import LanguageSharpIcon from '@mui/icons-material/LanguageSharp';
import OpenInNewOutlinedIcon from '@mui/icons-material/OpenInNewOutlined';
import PublishIcon from '@mui/icons-material/Publish';
import VisibilityIcon from '@mui/icons-material/Visibility';
import DataObjectIcon from '@mui/icons-material/DataObject';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, LinearProgress, Tab } from "@mui/material";
import JsonView from "@uiw/react-json-view";
import React, { useState } from 'react';
import { generateWebsiteFromAi, publishWebsite } from "../services/services";
import { styles } from "../style";
export default function Portfolio({ code, setCode }) {
  const [previewSrc, setPreviewSrc] = useState('');
  const [currentTab, setCurrentTab] = useState('json');
  const [tabs, setTabs] = useState([{ icon: <DataObjectIcon />, value: 'json' }]);
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(false);
  const [open, setOpen] = useState(false);
  const [siteId, setSiteId] = useState(null);
  React.useEffect(() => {
    const srcDoc = `${code?.html}`;
    setPreviewSrc(srcDoc);
  }, [code?.html]);

  const handleCodeChange = (type, e) => {
    if (e.type === "click") {
      setCurrentTab(type);
    } else if (type === "json") {
      const updatedJson = e.updated_src;
      setCode({ ...code, [type]: updatedJson });
    } else {
      setCode({ ...code, [type]: e.target.value });
    }
  };
  const openInNewTab = () => {
    const blob = new Blob([previewSrc], { type: "text/html" });
    const url = URL.createObjectURL(blob);
    window.open(url, "_blank");
  };

  const generateWebsite = async () => {
    try {
      if (code?.json) {
        setCode(prev => ({
          ...prev,
          html: ''
        }));
        setCurrentTab('json')
        setLoading(true)
        const res = await generateWebsiteFromAi(code?.content);
        const reader = res.body.getReader();
        const decoder = new TextDecoder("utf-8");
        // let done = false;
        while (true) {
          const { done, value } = await reader.read();
          if (done) break;

          const chunk = decoder.decode(value, { stream: true });
          const match = chunk.match(/siteId=([a-f0-9\-]+)/);
          const siteId = match ? match[1] : null;
          if (siteId) setSiteId(siteId);
          // Clean and parse SSE formatted data
          const cleanText = chunk
            .split("\n")
            .filter((line) => line.startsWith("data:")) // only keep data lines
            .map((line) => line.replace(/^data:\s?/, "").replace('```html', "").replace('```', "").replace(/siteId=([a-f0-9\-]+)/, "")) // remove "data:"
            .join("\n");

          if (cleanText.trim()) {
            setCode(prev => ({
              ...prev,
              html: prev.html + cleanText + "\n"
            }));
          }

        }
        if (!tabs.some(tab => tab.value === 'preview')) {
          setTabs([{ icon: <VisibilityIcon />, value: 'preview' }, { icon: <CodeIcon />, value: 'code' }, ...tabs])
        }
        setCurrentTab('preview')
        setLoading(false);
      }

    } catch (error) {
      console.log(error);
      setLoading(false);
    }
  }
  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(code.html);
      setCopied(true);
      setTimeout(() => setCopied(false), 1200); // Undo 'Copied!' after 1.2s
    } catch (err) {
      setCopied(false); // Could set to error state if needed
    }
  }
  const handleDeploy = async () => {
    try {
      if (!siteId) return;
      setLoading(true);
      let res = await publishWebsite(siteId);
      setCode(prev => ({
        ...prev,
        domainRes: res || {}
      }));
      setOpen(true)
      setLoading(false);
    } catch (err) {
      console.log(err, "error");
      setLoading(false);
    }
  };
  return (
    <>
      <Box sx={{ width: '100%', typography: 'body1' }}>
        <Box display="flex" justifyContent="normal">
          <Box sx={{ padding: '1rem' }}>
            {code?.content && <Button variant="outlined" startIcon={<LanguageSharpIcon />} style={{ color: 'rgb(35, 69, 103)' }} onClick={generateWebsite} disabled={loading} > {loading ? "Generating..." : "Generate Website"}</Button>}
          </Box>
          <Box sx={{ padding: '1rem' }}>
            {code?.html && <Button variant="outlined" startIcon={<PublishIcon />} style={{ color: 'rgb(35, 69, 103)' }} onClick={handleDeploy} disabled={loading} > {loading ? "Deploying..." : "Deploy"}</Button>}
          </Box>
        </Box>
        <TabContext value={currentTab} >
          <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <TabList onChange={(e, type) => handleCodeChange(type, e)} aria-label="lab API tabs example"
              TabIndicatorProps={{ style: { backgroundColor: "rgb(35,69,103)" } }}
            >
              {tabs?.map(({ icon, value }) => {
                return <Tab label={""} value={value} icon={icon} style={{ color: "rgb(35, 69, 103)" }} />
              })}
            </TabList>
            {loading && <LinearProgress variant="indeterminate" color="inherit" />}
          </Box>
          <TabPanel value="preview">
            <Button startIcon={<OpenInNewOutlinedIcon />} style={{ color: 'rgb(35, 69, 103)' }} onClick={openInNewTab}>Open Preview in New Tab</Button>
            <iframe
              title="Website Preview"
              srcDoc={previewSrc}
              style={styles.iframe}
              sandbox="allow-scripts allow-same-origin"
            />
          </TabPanel>
          <TabPanel value="code">
            <div style={{ position: "relative", borderRadius: "8px", overflow: "hidden" }}>
              <Editor
                options={{
                  fontSize: 14,
                  minimap: { enabled: false },
                  scrollBeyondLastLine: false,
                }}
                height="400px"
                theme="vs-dark"
                language="html"
                defaultValue=''
                value={code.html}
                onChange={(value) => handleCodeChange('html', { target: { value } })}
              />
              <button
                onClick={handleCopy}
                style={{
                  position: "absolute",
                  top: "10px",
                  right: "10px",
                  background: "#2d2d2d",
                  color: "#fff",
                  border: "none",
                  padding: "6px 10px",
                  borderRadius: "6px",
                  cursor: "pointer",
                  fontSize: "13px",
                  opacity: 0.8,
                  transition: "opacity 0.2s",
                }}
                onMouseEnter={(e) => (e.currentTarget.style.opacity = 1)}
                onMouseLeave={(e) => (e.currentTarget.style.opacity = 0.8)}
              >
                📋 {copied ? "Copied!" : "Copy"}
              </button>
            </div>
          </TabPanel>
          <TabPanel value="json">
            {/* <textarea
              rows={6}
              placeholder="File Content"
              value={code.content}
              onChange={(e) => handleCodeChange('content', e)}
              style={styles.textArea}
            /> */}
            <Box
              sx={{
                maxHeight: "60vh",
                overflowY: "auto",
                bgcolor: "#f9f9f9",
                borderRadius: 1,
                p: 1,
              }}
            >
              <JsonView
                value={code?.json}
                collapsed={false}
                displayDataTypes={false}
                style={{ fontSize: "14px" }}
                shortenTextAfterLength={false}
              />
            </Box>
          </TabPanel>
        </TabContext>

      </Box>
      <Dialog
        open={open}
        onClose={() => setOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle sx={{ color:code?.domainRes?.status?.toUpperCase()==="SUCCESS"?"green":"red", fontWeight: "bold" }}>
          {`Deployment is ${code?.domainRes?.status}`}
        </DialogTitle>

        <DialogContent dividers>
          <Box
            sx={{
              maxHeight: "60vh",
              overflowY: "auto",
              bgcolor: "#f9f9f9",
              borderRadius: 1,
              p: 1,
            }}
          >
            <JsonView
              value={code?.domainRes}
              collapsed={false}
              displayDataTypes={false}
              style={{ fontSize: "14px" }}
              shortenTextAfterLength={false}
            />
          </Box>
        </DialogContent>

        <DialogActions>
          <Button onClick={() => setOpen(false)} variant="outlined" color="secondary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );


}

