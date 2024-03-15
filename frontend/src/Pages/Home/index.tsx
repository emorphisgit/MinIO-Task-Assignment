import { Box, OutlinedInput } from '@mui/material';
import React, { ChangeEvent, Fragment, useEffect, useState } from 'react';
import TableLayout from '../../Component/Table'
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import api, { downloadFilesUrl, getFilesUrl, searchFilesUrl } from '../../http/config';
import { useDispatch } from 'react-redux';
import { setAlertData } from '../../Redux/slices/AlertPopUpSlice';

const Home = () => {
    const [open, setOpen] = React.useState<boolean>(false);
    const [searchTerm, setSearchTerm] = useState<string | number>('');
    const [downloadClickData, setDownloadClickData] = useState<any>({})
    const [filesData, setFilesData] = useState<any>([]);

    const dispatch = useDispatch()

    const handleDialogOpen = (data: any) => {
        setOpen(true);
        setDownloadClickData(data)
    };


    const handleDialogClose = () => {
        setOpen(false);
        setDownloadClickData({})
    };

    const handleSearchTextChange = (event: ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value)
    };

    useEffect(() => {
        const getFiles = async () => {
            try {
                const res = await api.get(getFilesUrl)
                // console.log('files',res.data)
                setFilesData(res?.data)
            } catch (error) {
                console.log(error)
            };
        }
        getFiles()
    }, [searchTerm])

    const handleDownloadClick = async () => {
        try {
            console.log("Here i am downloading!");
            const response = await api.get(`${downloadFilesUrl}/${downloadClickData?.fileName}`);
            //const blob = new Blob([response.data]);
            console.log(response);
            let mediaType = "";
            var blob = new Blob([response.data], {type: mediaType});
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', downloadClickData?.fileName);
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(url);

            dispatch(setAlertData({
                openAlert: true,
                alertMessage: 'file is downloading...',
                alertVariant: 'success'
            }))
        } catch (error) {
            console.error('Error downloading file:', error);
        }
        handleDialogClose()
    };


    const handleSearch = async () => {
        try {
            const res = await api.get(`${searchFilesUrl}/${searchTerm}`)
            setFilesData(res?.data);
        } catch (error) {
            console.log(error)
        };
    };

    return (
        <Fragment>
            <Box className="app">
                <Box sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItem: 'center',
                    justifyContent: 'center',
                    maxWidth: '800px',
                    margin: 'auto'
                }}
                >
                    <Box sx={{
                        display: 'flex',
                        padding: '10px 20px',
                        justifyContent: 'end',
                    }}>
                        <OutlinedInput
                            id="outlined-add-region"
                            type="text"
                            value={searchTerm}
                            placeholder='Write full filename'
                            onChange={handleSearchTextChange}
                            endAdornment={
                                <Button
                                    onClick={handleSearch}
                                >
                                    Search
                                </Button>
                            }
                        />
                    </Box>
                    <Box>
                        <TableLayout
                            files={filesData?.object || []}
                            handleDownloadClick={handleDialogOpen}
                        />
                    </Box>
                </Box>
            </Box>



            <Dialog
                open={open}
                onClose={handleDialogClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {/* {"Use Google's location service?"} */}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        Do you want to download this file?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDownloadClick}>Yes</Button>
                    <Button onClick={handleDialogClose} autoFocus>
                        No
                    </Button>
                </DialogActions>
            </Dialog>
        </Fragment>
    )
}

export default Home
