import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import React, { useState } from 'react';
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import DateTime from '../../Utils/DateTime.tsx';

export interface FilesType {
  fileName: string,
  id: string;
  description?: string;
  size: string | number;
  lastModified: string;
}

interface TableLayoutProps {
  files: FilesType[];
  handleDownloadClick: (row: any) => void;
}

const TableLayout: React.FC<TableLayoutProps> = ({ files, handleDownloadClick }) => {
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');

  const sortDataByName = () => {
    const sortedData = [...files].sort((a, b) => {
      if (sortDirection === 'asc') {
        return a.fileName?.localeCompare(b.fileName);
      } else {
        return b.fileName?.localeCompare(a.fileName);
      }
    });
    return sortedData;
  };

  const handleSortToggle = () => {
    setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
  };

  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      <TableContainer sx={{ maxHeight: '80vh' }}>
        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              <TableCell onClick={handleSortToggle}>
                <Box display="flex">
                  <Box>Name</Box>
                  <Box>{sortDirection === 'asc' ? <ArrowDownwardIcon className='arrowIcon' /> : <ArrowUpwardIcon className='arrowIcon' />}</Box>
                </Box>
              </TableCell>
              <TableCell>Size</TableCell>
              <TableCell>Last Modified</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sortDataByName().length > 0 ? (
              sortDataByName().map((row) => (
                <TableRow key={row?.id}>
                  <TableCell>{row?.fileName}</TableCell>
                  <TableCell>{row?.size}</TableCell>
                  <TableCell>
                    <DateTime datetime={row?.lastModified} />
                  </TableCell>
                  <TableCell align="left">
                    <ArrowDownwardIcon className='downLoadIcon' onClick={() => handleDownloadClick(row)} />
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={5} align="center">
                  <Typography variant="body1" textAlign="center">No data found</Typography>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  );
};

export default TableLayout;
