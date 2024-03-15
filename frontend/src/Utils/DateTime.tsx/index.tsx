import { Box, Typography } from '@mui/material'
import React from 'react'

interface DateTimeProps {
    datetime: string;
    sxProps?: any;
}
const DateTime: React.FC<DateTimeProps> = ({ datetime, sxProps }) => {
    const d = new Date(datetime)
    const date = d.toDateString()
    const time = d.toLocaleTimeString('en-US', { hour12: true });


    return (
        <Box sx={{ display: 'flex', ...sxProps }}>
            <Typography >{date} </Typography>
            <Typography ml={1}>{time}</Typography>
        </Box>
    )
}

export default DateTime
